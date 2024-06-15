package balbucio.org.firebase4j.model;

import balbucio.org.firebase4j.FirebaseAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
/**
 * Esta classe representa um usuário do Firebase.
 * Ela contém todos os dados do usuário, inclusive alguns sensiveis de login como o refreshToken e idToken.
 */
public class User {

    private FirebaseAuth instance;
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
     * PT-BR
     * Atualiza os detalhes do usuário.
     * EN-US
     * Updates the user's details.
     *
     * @throws Exception
     */
    public void updateDetails() throws Exception{
        instance.updateDetails(this, details);
    }

    /**
     * PT-BR
     * Adiciona e atualiza os detalhes do usuário.
     * EN-US
     * Adds and updates the user's details.
     *
     * @param displayName - nome de exibição do usuário.
     * @param photoUrl - url da foto de perfil do usuário
     * @throws Exception
     */
    public void updateDetails(String displayName, String photoUrl) throws Exception {
        this.details = new UserDetails(displayName, photoUrl);
        updateDetails();
    }

    /**
     * PT-BR
     * Envia o email de verificação
     * EN-US
     * Sends the email verification.
     *
     * @throws Exception
     */
    public void sendEmailVerification() throws Exception{
        instance.sendEmailVerification(this);
    }

    /**
     * PT-BR
     * Deleta este usuário.
     * EN-US
     * Deletes this user.
     *
     * @throws Exception
     */
    public void delete() throws Exception{
        instance.delete(this.getIdToken());
    }
}
