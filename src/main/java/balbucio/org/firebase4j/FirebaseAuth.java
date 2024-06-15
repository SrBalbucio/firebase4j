package balbucio.org.firebase4j;

import balbucio.org.firebase4j.exception.*;
import balbucio.org.firebase4j.impl.auth.AuthV1;
import balbucio.org.firebase4j.model.User;
import balbucio.org.firebase4j.model.UserDetails;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 * Classe com as definições do Firebase Auth.
 * <p>
 * A versão atual do Firebase Auth é a {@link AuthV1}.
 */
public abstract class FirebaseAuth {

    /**
     * Cria uma nova instância do Firebase Auth com a versão mais recente.
     *
     * @param options
     * @return
     */
    public static FirebaseAuth newInstance(FirebaseOptions options) {
        return new AuthV1(options);
    }

    protected String API_URL = "";
    protected FirebaseOptions options;
    protected User currentUser;

    public FirebaseAuth(FirebaseOptions options) {
        this.options = options;
        this.currentUser = options.getPersistent().getCurrentUser(this);
    }

    /**
     * Configura uma conexão HTTP para o Firebase Authentication API
     *
     * @param action - a ação que deve ser efetuada
     * @param method - o método HTTP que deve ser usado
     * @return - a conexão configurada para uso
     */
    public Connection getConnection(String action, Connection.Method method) {
        return Jsoup
                .connect(API_URL.replace("{action}", action).replace("{api_key}", options.getApiKey()))
                .header("Content-Type", "application/json")
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .method(method);
    }

    /**
     * PT-BR:
     * Cria um usuário com login anônimo.
     * EN-US:
     * Creates a user with an anonymous login.
     * <p>
     * PT-BR:
     * Caso queira associar um usuário a um identificador de autenticação, utilize o método {@link #linkUserWithEmailAndPassword(User, String, String)}.
     * EN-US:
     * If you want to associate a user with an authentication identifier, use the {@link #linkUserWithEmailAndPassword(User, String, String)} method.
     *
     * @return - created user
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link balbucio.org.firebase4j.exception.OperationNotAllowedException}
     */
    public abstract User signInAnonymously() throws Exception;

    /**
     * PT-BR
     * Cria um usuário com email e senha.
     * EN-US
     * Creates a user with email and password.
     *
     * @param email    - Email of User
     * @param password - Password of User
     * @return - created user
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link EmailExistsException}, {@link OperationNotAllowedException}
     */
    public abstract User signUp(String email, String password) throws Exception;

    /**
     * PT-BR
     * Efetua login a uma conta usando email e senha.
     * EN-US
     * Logs in to a user account using email and password.
     *
     * @param email    - Email of User
     * @param password - Password of User
     * @return - user logged in
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link UserNotFoundException}
     */
    public abstract User signIn(String email, String password) throws Exception;

    /**
     * PT-BR
     * Deleta um usuário com base no ID token fornecido.
     * EN-US
     * Deletes a user based on the provided ID token.
     *
     * @param idToken - ID token of User
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link InvalidIdTokenException}, {@link UserNotFoundException}
     */
    public abstract void delete(String idToken) throws Exception;
//    public abstract void sendPasswordResetEmail(String email) throws Exception;

    /**
     * PT-BR
     * Envia um email de verificação para o usuário fornecido.
     * EN-US
     * Sends a verification email to the provided user.
     *
     * @param user
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link InvalidIdTokenException}, {@link UserNotFoundException}
     */
    public abstract void sendEmailVerification(User user) throws Exception;

    /**
     * PT-BR
     * Atualiza os detalhes do usuário fornecido.
     * EN-US
     * Updates the details of the provided user.
     *
     * @param user
     * @param details - detalhes do usuário a serem atualizados
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link InvalidIdTokenException}, {@link UserNotFoundException}
     */
    public abstract void updateDetails(User user, UserDetails details) throws Exception;

    /**
     * PT-BR
     * Obtém os dados e os insere no user indicado. Geralmente os usuários não tem os dados definidos por padrão, portanto é importante efetuar esta chamada.
     * EN-US
     * Retrieves the user's data and inserts it into the indicated user. Generally, users do not have the data defined by default, so this call is essential.
     *
     * @param user - usuário que vai ter os dados carregados.
     * @throws Exception - em caso de erro na solicitação, pode retornar {@link InvalidIdTokenException}, {@link UserNotFoundException}
     */
    public abstract void getUserDetails(User user) throws Exception;

    /**
     * PT-BR
     * Associa um usuário a um identificador de autenticação usando email e senha.
     * EN-US
     * Links a user to an authentication identifier using email and password.
     *
     * @param user
     * @param email
     * @param password
     * @throws Exception
     */
    public abstract void linkUserWithEmailAndPassword(User user, String email, String password) throws Exception;

    /**
     * PT-BR
     * Deleta um usuário.
     * EN-US
     * Deletes a user.
     *
     * @param user - User
     * @throws Exception
     */
    public void deleteUser(User user) throws Exception {
        delete(user.getIdToken());
    }

    /**
     * PT-BR
     * Este método troca o usuário logado atual. Use o overwrite para sobrescrever se necessário.
     * Todos os métodos que contém algum nível de login utilizam este método automaticamente, porém com overwrite DESABILITADO.
     * Se o overwrite estiver desabilitado o {@link balbucio.org.firebase4j.exception.AlreadyLoggedException} será lançado, por meio dele você pode forçar um overwrite.
     *
     * @param user
     * @param overwrite
     */
    public void changeCurrentUser(User user, boolean overwrite) throws AlreadyLoggedException {
        if (currentUser != null && !overwrite) {
            throw new AlreadyLoggedException(user);
        }

        this.currentUser = user;
        options.getPersistent().saveCurrentUser(user);
    }

    public Exception processError(Connection.Response response, Object data) {
        JSONObject error = new JSONObject(response.body())
                .getJSONObject("error");
        String msg = error.getString("message");

        switch (msg) {
            case "OPERATION_NOT_ALLOWED":
                return new OperationNotAllowedException(msg);
            case "EMAIL_EXISTS":
                return new EmailExistsException(msg, ((JSONObject) data).getString("email"));
            case "INVALID_ID_TOKEN":
                return new InvalidIdTokenException(msg, ((JSONObject) data).getString("idToken"));
            case "USER_NOT_FOUND":
                return new UserNotFoundException(msg, ((JSONObject) data).getString("idToken"));
            default:
                return new RuntimeException(msg);
        }
    }

    @Override
    public String toString() {
        return "FirebaseAuth for " + options.getAppId();
    }
}
