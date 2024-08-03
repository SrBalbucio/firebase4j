package balbucio.org.firebase4j;

import balbucio.org.firebase4j.impl.firestore.FirestoreV1;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class Firestore {

    public static Firestore newInstance(FirebaseOptions options, String databaseName){
        return new FirestoreV1(options, databaseName);
    }

    protected String API_URL = "";
    protected FirebaseOptions options;
    protected String databaseName;

    protected Firestore(FirebaseOptions options, String databaseName) {
        this.options = options;
        this.databaseName = databaseName;
    }

    public Connection getConnection(String action, Connection.Method method){
        Connection connection = Jsoup.connect(API_URL.replace("{project_id}", options.getProjectId()).replace("{database_id}", databaseName));
        return null;
    }

    public abstract
}
