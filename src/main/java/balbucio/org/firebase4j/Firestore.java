package balbucio.org.firebase4j;

import balbucio.org.firebase4j.exception.PermissionDeniedException;
import balbucio.org.firebase4j.impl.firestore.FirestoreV1;
import balbucio.org.firebase4j.model.DocumentSnapshot;
import balbucio.org.firebase4j.model.FirestoreDatabase;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Firestore {

    public static Firestore newInstance(FirebaseOptions options){
        return newInstance(options, "(default)");
    }

    public static Firestore newInstance(FirebaseOptions options, String databaseName) {
        return newInstance(options, databaseName, null);
    }

    public static Firestore newInstance(FirebaseOptions options, String databaseName, FirebaseAuth auth) {
        return new FirestoreV1(options, databaseName).setAuth(auth);
    }

    protected String API_URL = "";
    protected String DATABASE_URL = "";
    protected FirebaseOptions options;
    protected String databaseName;
    protected FirebaseAuth auth;

    protected Firestore(FirebaseOptions options, String databaseName) {
        this.options = options;
        this.databaseName = databaseName;
    }

    public Firestore setAuth(FirebaseAuth auth) {
        this.auth = auth;
        return this;
    }

    public Connection getConnection(String action, Connection.Method method) {
        return Jsoup.connect(API_URL
                        .replace("{project_id}", options.getProjectId()))
                .header("Content-Type", "application/json")
                .method(method)
                .ignoreContentType(true)
                .ignoreHttpErrors(true);
    }

    public Connection getDbConnection(String action, Connection.Method method) {
        return getDbConnection(action, databaseName, method);
    }

    public Connection getDbConnection(String action, String databaseName, Connection.Method method) {
        return getDbConnection(action, databaseName, "", method);
    }

    public Connection getDbConnection(String action, String databaseName, String queryPath, Connection.Method method) {
        Connection connection = Jsoup.connect(DATABASE_URL
                        .replace("{query}", queryPath)
                        .replace("{project_id}", options.getProjectId())
                        .replace("{database_id}", databaseName)
                        .replace("{action}", action)
                        .replace("{API_KEY}", options.getApiKey())
                )
                .header("Content-Type", "application/json")
                .method(method)
                .ignoreContentType(true)
                .ignoreHttpErrors(true);

        if(auth.isLogged()){
            connection.header("Authorization", "Bearer "+auth.getCurrentUser().getIdToken());
        }

        return  connection;
    }

    public Exception processError(Connection.Response response, Object data) {
        JSONObject json = new JSONObject(response.body());

        if(json.has("error")){
            JSONObject error = json.getJSONObject("error");
            switch (error.optInt("code",  404)){
                case 403:
                    return new PermissionDeniedException(error.getInt("code"));
                default:
                    System.out.println(response.statusCode());
                    System.out.println(response.body());
                    return new RuntimeException("Este erro não foi identificado ou ainda não foi listado! Code: " + error.getInt("code"));
            }
        }
        return new RuntimeException("Este erro não foi identificado ou ainda não foi listado! Code: F4J-01");
    }

    public abstract List<FirestoreDatabase> listDatabases();

    public abstract DocumentSnapshot getDocument(String collection, String id) throws Exception;
    public abstract DocumentSnapshot getDocument(String database, String collection, String id) throws Exception;
    public abstract DocumentSnapshot updateDocument(String database, String collection, String id, Map<String, Object> fields) throws Exception;
    public abstract DocumentSnapshot updateDocument(String collection, String id, Map<String, Object> fields) throws Exception;
    public abstract void updateDocument(DocumentSnapshot documentSnapshot) throws Exception;
}
