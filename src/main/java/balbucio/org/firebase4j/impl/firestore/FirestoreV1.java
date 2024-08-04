package balbucio.org.firebase4j.impl.firestore;

import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.Firestore;
import balbucio.org.firebase4j.model.DocumentSnapshot;
import balbucio.org.firebase4j.model.FirestoreDatabase;
import org.json.JSONObject;
import org.jsoup.Connection;
import java.util.List;

public class FirestoreV1 extends Firestore {

    public FirestoreV1(FirebaseOptions options, String databaseName) {
        super(options, databaseName);
        API_URL = "https://firestore.googleapis.com/v1/projects/{project_id}/databases";
        DATABASE_URL = API_URL + "/{database_id}{action}?key={API_KEY}";
    }

    @Override
    public List<FirestoreDatabase> listDatabases() {
        return List.of();
    }

    @Override
    public DocumentSnapshot getDocument(String collection, String id) throws Exception {
        return getDocument(this.databaseName, collection, id);
    }

    @Override
    public DocumentSnapshot getDocument(String database, String collection, String id) throws Exception {
        Connection.Response response = getDbConnection("/documents/"+collection+"/"+id, database, Connection.Method.GET).execute();

        System.out.println(response.body());

        if (response.statusCode() != 200) {
            throw processError(response, null);
        }

        DocumentSnapshot documentSnapshot = new DocumentSnapshot();

        return documentSnapshot;
    }


}
