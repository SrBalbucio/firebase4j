package balbucio.org.firebase4j.server.impl;

import balbucio.org.firebase4j.server.FirebaseServerAppCheck;
import balbucio.org.firebase4j.server.FirebaseServerOptions;
import balbucio.org.firebase4j.server.model.AppCheckToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.NonNull;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class ServerAppCheckV1 extends FirebaseServerAppCheck {

    private Date jwtExpiresAt = new Date(0);
    private String jwtToken;

    public ServerAppCheckV1(@NonNull FirebaseServerOptions options) {
        super(options);
    }

    @Override
    public String getJWTToken() {
        Date now = new Date();
        if (jwtToken != null && jwtExpiresAt.after(now)) {
            return jwtToken;
        }

        String clientEmail = options.getServiceAccountJson().getString("client_email");
        long nowMs = System.currentTimeMillis();
        jwtExpiresAt = new Date(nowMs + 60 * 60 * 1000L);
        Algorithm algorithm = Algorithm.RSA256(null, options.getPrivateKey());
        this.jwtToken = JWT.create()
                .withIssuer(clientEmail)
                .withSubject(clientEmail)
                .withAudience("https://firebaseappcheck.googleapis.com/google.firebase.appcheck.v1.TokenExchangeService")
                .withIssuedAt(now)
                .withExpiresAt(jwtExpiresAt)
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

        if (response.statusCode() != 200) {
            return Optional.empty();
        }

        return Optional.of(options.getGson().fromJson(response.body(), AppCheckToken.class));
    }
}
