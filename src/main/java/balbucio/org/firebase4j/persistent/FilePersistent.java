package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * PT-BR
 * Esta classe permite a persistência de dados por meio de um arquivo JSON simples.
 * <p>
 * EN-US
 * This class allows persistent data through a simple JSON file.
 */
public class FilePersistent implements FirebasePersistent {

    private File file;
    private JSONObject data;

    public FilePersistent(File file) {
        this.file = file;
        prepare();
    }

    private void prepare() {
        try {
            if (!file.exists()) {
                file.createNewFile();
                this.data = new JSONObject();
                return;
            }

            this.data = new JSONObject(new JSONTokener(new FileReader(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveAsync() {
        Executors.newCachedThreadPool().execute(() -> {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write(data.toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public User getCurrentUser(FirebaseAuth auth) {
        if (!data.has("currentUser")) {
            return null;
        }

        User user = new User();
        user.setInstance(auth);
        JSONObject userData = data.getJSONObject("currentUser");
        user.setIdToken(userData.getString("idToken"));
        user.setRefreshToken(userData.getString("refreshToken"));
        user.setEmail(userData.optString("email"));
        user.setLocalId(userData.optString("localId"));
        return user;
    }

    @Override
    public void saveCurrentUser(User user) {
        JSONObject userData = new JSONObject();
        userData.put("idToken", user.getIdToken());
        userData.put("refreshToken", user.getRefreshToken());
        userData.put("email", user.getEmail());
        userData.put("localId", user.getLocalId());
        data.put("currentUser", userData);
        saveAsync();
    }

    @Override
    public void clear() {
        this.data = new JSONObject();
        file.delete();
    }
}
