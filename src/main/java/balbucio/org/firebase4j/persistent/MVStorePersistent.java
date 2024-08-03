package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;

import java.io.File;

public class MVStorePersistent implements FirebasePersistent{

    private File file;

    public MVStorePersistent(File file){
        this.file = file;
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
