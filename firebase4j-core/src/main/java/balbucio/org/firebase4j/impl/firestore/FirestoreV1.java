package balbucio.org.firebase4j.impl.firestore;

import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.Firestore;
import balbucio.org.firebase4j.model.DocumentSnapshot;
import balbucio.org.firebase4j.model.FirestoreDatabase;
import lombok.NonNull;
import org.json.JSONObject;
import org.jsoup.Connection;

import java.util.List;
import java.util.Map;

public class FirestoreV1 extends Firestore {

    public FirestoreV1(@NonNull FirebaseOptions options, @NonNull String databaseName) {
        super(options, databaseName);
        API_URL = "https://firestore.googleapis.com/v1/projects/{project_id}/databases";
        DATABASE_URL = API_URL + "/{database_id}{action}?key={API_KEY}{query}";
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
        Connection.Response response = getDbConnection("/documents/" + collection + "/" + id, database, Connection.Method.GET).execute();

        if (response.statusCode() != 200) {
            throw processError(response, null);
        }

        JSONObject payload = new JSONObject(response.body());
        DocumentSnapshot documentSnapshot = new DocumentSnapshot(collection, database);
        documentSnapshot.fromPayload(payload);

        return documentSnapshot;
    }

    @Override
    public DocumentSnapshot updateDocument(String collection, String id, Map<String, Object> fields) throws Exception {
        return updateDocument(databaseName, collection, id, fields);
    }

    @Override
    public DocumentSnapshot updateDocument(String database, String collection, String id, Map<String, Object> fields) throws Exception {
        Connection.Response response = getDbConnection("/documents/" + collection + "/" + id, database,
                Connection.Method.PATCH)
                .requestBody(new JSONObject()
                        .put("fields", fields)
                        .toString())
                .execute();

        if (response.statusCode() != 200) {
            throw processError(response, null);
        }

        JSONObject payload = new JSONObject(response.body());
        DocumentSnapshot documentSnapshot = new DocumentSnapshot(database, collection);
        documentSnapshot.fromPayload(payload);

        return documentSnapshot;
    }

    @Override
    public void updateDocument(DocumentSnapshot documentSnapshot) throws Exception {
        DocumentSnapshot snapshot = updateDocument(
                documentSnapshot.getDatabase(),
                documentSnapshot.getCollection(),
                documentSnapshot.getName(),
                documentSnapshot.toPayload());

        documentSnapshot.replaceValues(snapshot);
    }


}
