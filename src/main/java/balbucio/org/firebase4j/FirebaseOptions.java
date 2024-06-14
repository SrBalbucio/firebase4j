package balbucio.org.firebase4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@AllArgsConstructor
@Data
public class FirebaseOptions {

    public static FirebaseOptions fromJSON(JSONObject json){
        FirebaseOptions options = new FirebaseOptions(
                new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create(),
                json.getString("apiKey"),
                json.getString("authDomain"),
                json.getString("databaseURL"),
                json.getString("projectId"),
                json.getString("storageBucket"),
                json.getString("messagingSenderId"),
                json.getString("appId"),
                json.getString("measurementId"),
                null
        );

        return options;
    }

    public static FirebaseOptions fromJsonFile(File path) throws FileNotFoundException {
        JSONObject json = new JSONObject(new FileReader(path));
        return fromJSON(json);
    }

    private Gson gson;
    private String apiKey;
    private String authDomain;
    private String databaseURL;
    private String projectId;
    private String storageBucket;
    private String messagingSenderId;
    private String appId;
    private String measurementId;
    private String serviceAccount;
}
