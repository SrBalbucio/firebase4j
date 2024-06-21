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

    public static User getWithRefreshToken(String refreshToken){
        return null;
    }

    public static User withIdToken(String idToken){
        return new User(idToken);
    }

    private FirebaseAuth instance;
    @NonNull
    private String idToken;
    private String refreshToken;
    private long expiresIn;
    private String localId;
    private String email;
    private boolean emailVerified;
    private UserDetails details;
    private double passwordUpdatedAt;
    private boolean disabled;
    private String validSince;
    private String lastLoginAt;
    private String createdAt;
    private boolean customAuth = false;

    /**
     * <h3>PT-BR</h3>
     * <p>
     * Atualiza os detalhes do usuário.
     * </p>
     * <h3>EN-US</h3>
     * <p>Updates the user's details.</p>
     *
     * @throws Exception
     */
    public void updateDetails() throws Exception {
        instance.updateDetails(this, details);
    }

    /**
     * <h3>PT-BR</h3>
     * <p>Adiciona e atualiza os detalhes do usuário.</p>
     * <h3>EN-US</h3>
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
     * <h3>PT-BR</h3>
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
     * <h3>PT-BR</h3>
     * <p>Deleta este usuário.</p>
     * <h3>EN-US</h3>
     * <p>Deletes this user.</p>
     *
     * @throws Exception
     */
    public void delete() throws Exception {
        instance.delete(this.getIdToken());
    }
}
