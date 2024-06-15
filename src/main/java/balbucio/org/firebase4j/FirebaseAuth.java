package balbucio.org.firebase4j;

import balbucio.org.firebase4j.exception.EmailExistsException;
import balbucio.org.firebase4j.exception.InvalidIdTokenException;
import balbucio.org.firebase4j.exception.OperationNotAllowedException;
import balbucio.org.firebase4j.exception.UserNotFoundException;
import balbucio.org.firebase4j.impl.auth.AuthV1;
import balbucio.org.firebase4j.model.User;
import balbucio.org.firebase4j.model.UserDetails;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class FirebaseAuth {

    public static FirebaseAuth newInstance(FirebaseOptions options){
        return new AuthV1(options);
    }

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
//    public abstract void sendPasswordResetEmail(String email) throws Exception;
    public abstract void sendEmailVerification(User user) throws Exception;
    public abstract void updateDetails(User user, UserDetails details) throws Exception;
    public abstract void getUserDetails(User user) throws Exception;

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
            case "EMAIL_EXISTS":
                return new EmailExistsException(msg, ((JSONObject) data).getString("email"));
            case "INVALID_ID_TOKEN":
                return new InvalidIdTokenException(msg, ((JSONObject) data).getString("idToken"));
            case "USER_NOT_FOUND":
                return new UserNotFoundException(msg, ((JSONObject) data).getString("idToken"));
            default:
                return new RuntimeException(msg);
        }
    }

    @Override
    public String toString() {
        return "FirebaseAuth for "+options.getAppId();
    }
}
