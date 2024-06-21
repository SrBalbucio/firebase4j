package balbucio.org.firebase4j.impl.firestore;

import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.Firestore;

public class FirestoreV1 extends Firestore {

    public FirestoreV1(FirebaseOptions options, String databaseName) {
        super(options, databaseName);
        API_URL = "https://firestore.googleapis.com/v1/projects/{project_id}/databases/{database_id}";
    }
}
