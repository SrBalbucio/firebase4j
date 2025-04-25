package balbucio.org.firebase4j;

import balbucio.org.firebase4j.persistent.EmptyPersistent;
import balbucio.org.firebase4j.persistent.FirebasePersistent;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Data
public class FirebaseOptions {

    static {
        allowMethods("PATCH");
    }

    public static FirebaseOptions fromJSON(JSONObject json) {
        FirebaseOptions options = new FirebaseOptions(
                new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create(),
                json.getString("apiKey"),
                json.getString("authDomain"),
                json.optString("databaseURL", json.optString("databaseUrl", "")),
                json.optString("projectId", json.optString("id")),
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

    public static FirebaseOptions fromClasspath(String path) {
        JSONObject json = new JSONObject(
                new JSONTokener(Objects.requireNonNull(FirebaseOptions.class.getResourceAsStream(path))));
        return fromJSON(json);
    }

    @NonNull
    private Gson gson;
    /**
     * -- SETTER --
     * <h3>PT-BR</h3>
     * <p>
     * Caso deseje manter o estado atual das informações, como o usuário logado, as informações dele e afins, é necessário adicionar algum tipo de persistência.
     * Você pode optar por algum desses:
     * ,
     * </p>
     * <h3>EN-US</h3>
     * <p>
     * In case you want to keep the current state of information, such as the logged-in user's information and so on, you need to add some type of persistence.
     * You can choose among:
     * ,
     * </p>
     *
     * @param persistent
     */
    private FirebasePersistent persistent = new EmptyPersistent();
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
    @NonNull
    private String emailTest;
    @Getter
    private JSONObject serviceAccount = null;
    @Getter
    private GoogleCredentials serviceAccountCredentials = null;
    @Getter
    private RSAPrivateKey privateKey;

    public FirebaseOptions(@NonNull Gson gson, @NonNull String apiKey, @NonNull String authDomain, @NonNull String databaseURL, @NonNull String projectId, @NonNull String storageBucket, @NonNull String messagingSenderId, @NonNull String appId, @NonNull String measurementId, String emailTest) {
        this.gson = gson;
        this.apiKey = apiKey;
        this.authDomain = authDomain;
        this.databaseURL = databaseURL;
        this.projectId = projectId;
        this.storageBucket = storageBucket;
        this.messagingSenderId = messagingSenderId;
        this.appId = appId;
        this.measurementId = measurementId;
        this.emailTest = emailTest;
    }

    public FirebaseOptions withServiceAccount(JSONObject serviceAccount) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.serviceAccount = serviceAccount;
        this.serviceAccountCredentials = GoogleCredentials.fromStream(new ByteArrayInputStream(serviceAccount.toString().getBytes()))
                .createScoped("https://www.googleapis.com/auth/firebase");
        createRSAKey();
        return this;
    }

    public FirebaseOptions withServiceAccount(InputStream serviceAccount) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.serviceAccount = new JSONObject(new JSONTokener(serviceAccount));
        this.serviceAccountCredentials = GoogleCredentials.fromStream(serviceAccount)
                .createScoped("https://www.googleapis.com/auth/firebase");
        createRSAKey();
        return this;
    }

    public FirebaseOptions withServiceAccount(File path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        withServiceAccount(new JSONObject(new JSONTokener(new FileReader(path))));
        return this;
    }

    private void createRSAKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPem = serviceAccount.getString("private_key");

        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] pkcs8 = Base64.getDecoder().decode(privateKeyPem);
        this.privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(pkcs8));
    }

    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
