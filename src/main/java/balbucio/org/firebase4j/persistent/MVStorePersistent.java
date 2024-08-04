package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;
import org.h2.mvstore.MVStore;

import java.io.File;

public class MVStorePersistent implements FirebasePersistent{

    private File file;
    private MVStore store;

    public MVStorePersistent(File file){
        this(file, null);
    }

    public MVStorePersistent(File file, String password){
        this.file = file;
        MVStore.Builder builder = new MVStore.Builder()
                .fileName(file.getAbsolutePath())
                .compress()
                .autoCommitDisabled()
                .autoCompactFillRate(90)
                .pageSplitSize(65536);

        if(password != null){
            builder.encryptionKey(password.toCharArray());
        }

        this.store = builder.open();
    }

    @Override
    public User getCurrentUser(FirebaseAuth auth) {
        return null;
    }

    @Override
    public void saveCurrentUser(User user) {

    }

    @Override
    public void removeCurrentUser() {

    }

    @Override
    public void clear() {

    }
}
