package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;
import balbucio.org.firebase4j.model.UserDetails;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;

import java.io.File;

public class MVStorePersistent implements FirebasePersistent {

    private File file;
    private MVStore store;

    public MVStorePersistent(File file) {
        this(file, null);
    }

    public MVStorePersistent(File file, String password) {
        this.file = file;
        MVStore.Builder builder = new MVStore.Builder()
                .fileName(file.getAbsolutePath())
                .compress()
                .autoCommitDisabled()
                .autoCompactFillRate(90)
                .pageSplitSize(65536);

        if (password != null) {
            builder.encryptionKey(password.toCharArray());
        }

        this.store = builder.open();
    }

    @Override
    public User getCurrentUser(FirebaseAuth auth) {
        if (!store.hasMap("userData")) {
            return null;
        }

        MVMap<String, String> userData = store.openMap("userData");
        User user = new User();
        user.setInstance(auth);
        user.setIdToken(userData.get("idToken"));
        user.setRefreshToken(userData.get("refreshToken"));
        user.setEmail(userData.getOrDefault("email", ""));
        user.setLocalId(userData.get("localId"));

        if (store.hasMap("userDetails")) {
            MVMap<String, String> dd = store.openMap("userDetails");
            UserDetails details = new UserDetails();
            details.setDisplayName(dd.get("displayName"));
            details.setPhotoUrl(dd.get("photoUrl"));
        }

        return user;
    }

    @Override
    public void saveCurrentUser(User user) {
        MVMap<String, String> userData = store.openMap("userData");
        userData.put("idToken", user.getIdToken());
        userData.put("refreshToken", user.getRefreshToken());
        if (!user.isAnonymous()) {
            userData.put("email", user.getEmail());
        }
        userData.put("localId", user.getLocalId());

        if (user.detailsIsPresent()) {
            MVMap<String, String> details = store.openMap("userDetails");
            details.put("photoUrl", user.getDetails().getPhotoUrl());
            details.put("displayName", user.getDetails().getDisplayName());
        }

        store.tryCommit();
    }

    @Override
    public void removeCurrentUser() {
        store.removeMap("userData");
        store.tryCommit();
    }

    @Override
    public void clear() {
        // por enquanto não vou implementar isto
    }
}
