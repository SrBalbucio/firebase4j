package balbucio.org.firebase4j.impl.auth;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.model.User;
import org.json.JSONObject;
import org.jsoup.Connection;

public class AuthV1 extends FirebaseAuth {


    public AuthV1(FirebaseOptions options) {
        super(options);
        API_URL = "https://identitytoolkit.googleapis.com/v1/accounts:{action}?key={api_key}";
    }

    @Override
    public User signInAnonymously() throws Exception {
        Connection.Response response = getConnection("signUp", Connection.Method.POST)
                .requestBody(new JSONObject()
                        .put("returnSecureToken", true)
                        .toString()).execute();

        if (response.statusCode() != 200) {
            throw processError(response, null);
        }

        User user = options.getGson().fromJson(response.body(), User.class);
        user.setInstance(this);
        return user;
    }

    @Override
    public User signUp(String email, String password) throws Exception {
        JSONObject data = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);

        Connection.Response response = getConnection("signUp", Connection.Method.POST)
                .requestBody(data.toString())
                .execute();

        if (response.statusCode() != 200) {
            throw processError(response, data);
        }

        User user = options.getGson().fromJson(response.body(), User.class);
        user.setInstance(this);
        return user;
    }

    @Override
    public User signIn(String email, String password) throws Exception {
        JSONObject data = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("returnSecureToken", true);

        Connection.Response response = getConnection("signInWithPassword", Connection.Method.POST)
                .requestBody(data.toString())
                .execute();

        if (response.statusCode() != 200) {
            throw processError(response, data);
        }

        User user = options.getGson().fromJson(response.body(), User.class);
        user.setInstance(this);
        return user;
    }

    @Override
    public void delete(String idToken) throws Exception {
        JSONObject data = new JSONObject()
                .put("idToken", idToken);
        Connection.Response response = getConnection("delete", Connection.Method.POST)
                .requestBody(data.toString())
                .execute();

        if (response.statusCode() != 200) {
            throw processError(response, data);
        }

    }

    @Override
    public void sendEmailVerification(User user) throws Exception {
        JSONObject data = new JSONObject()
                .put("requestType", "VERIFY_EMAIL")
                .put("idToken", user.getIdToken());

        Connection.Response response = getConnection("sendOobCode", Connection.Method.POST)
                .requestBody(data.toString()).execute();

        if (response.statusCode() != 200) {
            throw processError(response, data);
        }
    }
}
