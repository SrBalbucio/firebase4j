package balbucio.org.firebase4j;

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
                .ignoreContentType(true)
                .method(method);
    }

    public abstract User signInAnonymously();

    public abstract User signUp(String email, String password);
    public abstract User signIn(String email, String password);


    public static class V1 extends FirebaseAuth {


        public V1(FirebaseOptions options) {
            super(options);
            API_URL = "https://identitytoolkit.googleapis.com/v1/accounts:{action}?key={api_key}";
        }

        @Override
        public User signInAnonymously() {
            try {
                String response = getConnection("signUp", Connection.Method.POST)
                        .requestBody(new JSONObject()
                                .put("returnSecureToken", true)
                                .toString())
                        .execute().body();

                User user = options.getGson().fromJson(response, User.class);
                return user;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public User signUp(String email, String password) {
            try {
                String response = getConnection("signUp", Connection.Method.POST)
                        .requestBody(new JSONObject()
                                .put("email", email)
                                .put("password", password)
                                .put("returnSecureToken", true)
                                .toString())
                        .execute()
                        .body();

                User user = options.getGson().fromJson(response, User.class);
                return user;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public User signIn(String email, String password) {
            try {
                String response = getConnection("signInWithPassword", Connection.Method.POST)
                        .requestBody(new JSONObject()
                                .put("email", email)
                                .put("password", password)
                                .put("returnSecureToken", true)
                                .toString())
                        .execute()
                        .body();

                User user = options.getGson().fromJson(response, User.class);
                return user;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
