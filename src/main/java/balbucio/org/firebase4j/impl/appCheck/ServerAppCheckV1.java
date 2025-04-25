package balbucio.org.firebase4j.impl.appCheck;

import balbucio.org.firebase4j.FirebaseServerAppCheck;
import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.model.AppCheckToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.auth.oauth2.GoogleCredentials;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class ServerAppCheckV1 extends FirebaseServerAppCheck {
    public ServerAppCheckV1(FirebaseOptions options) {
        super(options);
    }

    private Date nextTokenUpdate = new Date();
    private String jwtToken;

    @Override
    public String getJWTToken() {
        if(nextTokenUpdate.before(new Date()) && jwtToken != null) {
            return jwtToken;
        }

        String clientEmail = options.getServiceAccount().getString("client_email");
        long now = System.currentTimeMillis();
        Algorithm algorithm = Algorithm.RSA256(null, options.getPrivateKey());
        nextTokenUpdate = new Date(now + 60 * 60 * 1000);
        this.jwtToken = JWT.create()
                .withIssuer(clientEmail)
                .withSubject(clientEmail)
                .withAudience("https://firebaseappcheck.googleapis.com/google.firebase.appcheck.v1.TokenExchangeService")
                .withIssuedAt(new Date(now))
                .withExpiresAt(nextTokenUpdate)
                .withClaim("app_id", options.getAppId())
                .sign(algorithm);
        return jwtToken;
    }

    @Override
    public Optional<AppCheckToken> createToken(boolean limitedUse) throws IOException {
        GoogleCredentials credentials = options.getServiceAccountCredentials();
        credentials.refreshIfExpired();
        String accessToken = credentials.getAccessToken().getTokenValue();

        String url = String.format(
                "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken",
                options.getProjectId(), options.getAppId());

        Connection connection = Jsoup.connect(url)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .requestBody(new JSONObject()
                        .put("customToken", getJWTToken())
                        .put("limitedUse", limitedUse)
                        .toString());

        Connection.Response response = connection.execute();
        System.out.println(response.body());
        if(response.statusCode() != 200) {
            return Optional.empty();
        }

        return Optional.of(options.getGson().fromJson(response.body(), AppCheckToken.class));
    }
}
