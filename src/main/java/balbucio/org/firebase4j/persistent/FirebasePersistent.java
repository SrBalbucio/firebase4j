package balbucio.org.firebase4j.persistent;

import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.model.User;

import java.io.File;

public interface FirebasePersistent {

    /**
     * <h1>PT-BR</h1>
     * <p>Recupera o usuário logado atual.</p>
     * <h1>EN-US</h1>
     * <p>Retrieves the currently logged in user.</p>
     *
     * @param auth
     * @return
     */
    public User getCurrentUser(FirebaseAuth auth);

    /**
     * <h1>PT-BR</h1>
     * <p>Salva um usuário como o atualmente logado.</p>
     * <h1>EN-US</h1>
     * <p>Saves the currently logged in user.</p>
     *
     * @param user
     */
    public void saveCurrentUser(User user);

    /**
     * <h1>PT-BR</h1>
     * <p>Remove o usuário atual da persistência.
     * É ideal usar o {@link FirebaseAuth#logout()} se for para remover o usuário atual logado.</p>
     * <h1>EN-US</h1>
     * <p>Removes the currently logged in user from persistence.</p>
     */
    public void removeCurrentUser();

    /**
     * <h1>PT-BR</h1>
     * <p>Limpa todos os dados salvos.</p>
     * <h1>EN-US</h1>
     * <p>Clears all saved data.</p>
     */
    public void clear();

    public static FirebasePersistent fromFile(File file) {
        return new FilePersistent(file);
    }
}
