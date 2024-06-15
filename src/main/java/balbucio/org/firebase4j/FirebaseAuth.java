package balbucio.org.firebase4j;

import balbucio.org.firebase4j.exception.EmailExistsException;
import balbucio.org.firebase4j.exception.OperationNotAllowedException;
import balbucio.org.firebase4j.model.User;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public abstract class FirebaseAuth {

    protected String API_URL = "";
    protected FirebaseOptions options;

    public FirebaseAuth(FirebaseOptions options) {
        this.options = options;
    }

    public Connection getConnection(String action, Connection.Method method) {
        return Jsoup
                .connect(API_URL.replace("{action}", action).replace("{api_key}", options.getApiKey()))
                .header("Content-Type", "application/json")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(method);
    }

    public abstract User signInAnonymously() throws Exception;

    public abstract User signUp(String email, String password) throws Exception;

    public abstract User signIn(String email, String password) throws Exception;

    public abstract void delete(String idToken) throws Exception;

    public void deleteUser(User user) throws Exception {
        delete(user.getIdToken());
    }

    public Exception processError(Connection.Response response, Object data) {
        JSONObject error = new JSONObject(response.body())
                .getJSONObject("error");
        String msg = error.getString("message");

        switch (msg) {
            case "OPERATION_NOT_ALLOWED":
                return new OperationNotAllowedException(msg);
            case "":
                return new EmailExistsException(msg, ((JSONObject) data).getString("email"));
            default:
                return new RuntimeException(msg);
        }
    }

    public static class V1 extends FirebaseAuth {


        public V1(FirebaseOptions options) {
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
    }
}
