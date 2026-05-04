package balbucio.org.firebase4j.server;

import balbucio.org.firebase4j.FirebaseOptions;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Service-account-backed options for server-side Firebase APIs (e.g. App Check token exchange).
 * Use {@link #fromClientAndServiceAccount(FirebaseOptions, File)} to combine web client config with a JSON key.
 */
public final class FirebaseServerOptions {

    @Getter
    @NonNull
    private final Gson gson;
    @Getter
    @NonNull
    private final String projectId;
    @Getter
    @NonNull
    private final String appId;
    @Getter
    private final JSONObject serviceAccountJson;
    @Getter
    private final GoogleCredentials serviceAccountCredentials;
    @Getter
    private final RSAPrivateKey privateKey;

    private FirebaseServerOptions(
            @NonNull Gson gson,
            @NonNull String projectId,
            @NonNull String appId,
            @NonNull JSONObject serviceAccountJson,
            @NonNull GoogleCredentials serviceAccountCredentials,
            @NonNull RSAPrivateKey privateKey
    ) {
        this.gson = gson;
        this.projectId = projectId;
        this.appId = appId;
        this.serviceAccountJson = serviceAccountJson;
        this.serviceAccountCredentials = serviceAccountCredentials;
        this.privateKey = privateKey;
    }

    public static FirebaseServerOptions fromClientAndServiceAccount(
            @NonNull FirebaseOptions client,
            @NonNull File serviceAccountJsonFile
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (FileReader reader = new FileReader(serviceAccountJsonFile)) {
            return fromClientAndServiceAccount(client, new JSONObject(new JSONTokener(reader)));
        }
    }

    public static FirebaseServerOptions fromClientAndServiceAccount(
            @NonNull FirebaseOptions client,
            @NonNull InputStream serviceAccountJsonStream
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return fromClientAndServiceAccount(client, new JSONObject(new JSONTokener(serviceAccountJsonStream)));
    }

    public static FirebaseServerOptions fromClientAndServiceAccount(
            @NonNull FirebaseOptions client,
            @NonNull JSONObject serviceAccountJson
    ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(serviceAccountJson.toString().getBytes()))
                .createScoped("https://www.googleapis.com/auth/firebase");
        RSAPrivateKey rsaPrivateKey = parsePrivateKey(serviceAccountJson);
        return new FirebaseServerOptions(
                client.getGson(),
                client.getProjectId(),
                client.getAppId(),
                serviceAccountJson,
                credentials,
                rsaPrivateKey
        );
    }

    private static RSAPrivateKey parsePrivateKey(JSONObject serviceAccount) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPem = serviceAccount.getString("private_key");
        privateKeyPem = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] pkcs8 = Base64.getDecoder().decode(privateKeyPem);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
    }
}
