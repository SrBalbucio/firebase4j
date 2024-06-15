package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;

import java.io.File;

public interface FirebasePersistent {

    /**
     * PT-BR
     * Recupera o usuário logado atual.
     * EN-US
     * Retrieves the currently logged in user.
     *
     * @param auth
     * @return
     */
    public User getCurrentUser(FirebaseAuth auth);

    /**
     * Salva um usuário como o atualmente logado.
     * @param user
     */
    public void saveCurrentUser(User user);

    public static FirebasePersistent fromFile(File file){
        return new FilePersistent(file);
    }
}
