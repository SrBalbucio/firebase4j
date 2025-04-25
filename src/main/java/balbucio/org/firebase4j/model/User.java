package balbucio.org.firebase4j.model;

import balbucio.org.firebase4j.FirebaseAuth;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString
/**
 * Esta classe representa um usuário do Firebase.
 * Ela contém todos os dados do usuário, inclusive alguns sensiveis de login como o refreshToken e idToken.
 */
public class User {

    public static User getWithRefreshToken(String refreshToken) {
        return null;
    }

    public static User withIdToken(String idToken) {
        return new User(idToken);
    }

    private FirebaseAuth instance;
    @NonNull
    private String idToken;
    private String refreshToken;
    private long expiresIn;
    private String localId;
    private String email = "";
    private boolean emailVerified;
    private UserDetails details;
    private double passwordUpdatedAt;
    private boolean disabled;
    private String validSince;
    private String lastLoginAt;
    private String createdAt;
    private boolean customAuth = false;

    /**
     * <h1>PT-BR</h1>
     * <p>
     * Utilize este método para saber se os detalhes do usuário já foram carregados e são válidos.
     * </p>
     * <h1>EN-US</h1>
     * <p>Use this method to find out if the user details have already been loaded and are valid.</p>
     *
     * @return
     */
    public boolean detailsIsPresent() {
        return details != null;
    }

    public boolean isAnonymous(){
        return email.isEmpty() || email == null;
    }

    /**
     * <h1>PT-BR</h1>
     * <p>
     * Atualiza os detalhes do usuário.
     * </p>
     * <h1>EN-US</h1>
     * <p>Updates the user's details.</p>
     *
     * @throws Exception
     */
    public void updateDetails() throws Exception {
        instance.updateDetails(this, details);
    }

    /**
     * <h1>PT-BR</h1>
     * <p>Adiciona e atualiza os detalhes do usuário.</p>
     * <h1>EN-US</h1>
     * <p>Adds and updates the user's details.</p>
     *
     * @param displayName - nome de exibição do usuário.
     * @param photoUrl    - url da foto de perfil do usuário
     * @throws Exception
     */
    public void updateDetails(String displayName, String photoUrl) throws Exception {
        this.details = new UserDetails(displayName, photoUrl);
        updateDetails();
    }

    /**
     * <h1>PT-BR</h1>
     * <p>Envia o email de verificação para o usuário.</p>
     * EN-US
     * <p>Sends the email verification for the user.</p>
     *
     * @throws Exception
     */
    public void sendEmailVerification() throws Exception {
        instance.sendEmailVerification(this);
    }

    /**
     * <h1>PT-BR</h1>
     * <p>Deleta este usuário.</p>
     * <h1>EN-US</h1>
     * <p>Deletes this user.</p>
     *
     * @throws Exception
     */
    public void delete() throws Exception {
        instance.delete(this.getIdToken());
    }
}
