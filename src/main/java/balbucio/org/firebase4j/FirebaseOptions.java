package balbucio.org.firebase4j;

import balbucio.org.firebase4j.model.User;
import balbucio.org.firebase4j.persistent.FirebasePersistent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@RequiredArgsConstructor
@Data
public class FirebaseOptions {

    public static FirebaseOptions fromJSON(JSONObject json){
        FirebaseOptions options = new FirebaseOptions(
                new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create(),
                json.getString("apiKey"),
                json.getString("authDomain"),
                json.optString("databaseURL", ""),
                json.getString("projectId"),
                json.optString("storageBucket", ""),
                json.getString("messagingSenderId"),
                json.getString("appId"),
                json.getString("measurementId"),
                json.optString("emailTest", "dev65@dev.com")
        );

        return options;
    }

    public static FirebaseOptions fromJsonFile(File path) throws FileNotFoundException {
        JSONObject json = new JSONObject(new JSONTokener(new FileReader(path)));
        return fromJSON(json);
    }

    @NonNull
    private Gson gson;
    private FirebasePersistent persistent = new FirebasePersistent() {
        @Override
        public User getCurrentUser(FirebaseAuth auth) {
            return null;
        }

        @Override
        public void saveCurrentUser(User user) {

        }
    };
    @NonNull
    private String apiKey;
    @NonNull
    private String authDomain;
    @NonNull
    private String databaseURL;
    @NonNull
    private String projectId;
    @NonNull
    private String storageBucket;
    @NonNull
    private String messagingSenderId;
    @NonNull
    private String appId;
    @NonNull
    private String measurementId;
    private String serviceAccount = null;
    @NonNull
    private String emailTest;

    /**
     * <h3>PT-BR</h3>
     * <p>
     * Caso deseje manter o estado atual das informações, como o usuário logado, as informações dele e afins, é necessário adicionar algum tipo de persistência.
     * Você pode optar por algum desses: {@link balbucio.org.firebase4j.persistent.FilePersistent}
     * </p>
     * <h3>EN-US</h3>
     * <p>
     * In case you want to keep the current state of information, such as the logged-in user's information and so on, you need to add some type of persistence.
     * You can choose among: {@link balbucio.org.firebase4j.persistent.FilePersistent}
     * </p>
     * @param persistent
     */
    public void setPersistent(FirebasePersistent persistent) {
        this.persistent = persistent;
    }

}
