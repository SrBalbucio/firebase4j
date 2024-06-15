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

    public void updateDetails() throws Exception{
        instance.updateDetails(this, details);
    }

    public void updateDetails(String displayName, String photoUrl) throws Exception {
        this.details = new UserDetails(displayName, photoUrl);
        updateDetails();
    }

    public void sendEmailVerification() throws Exception{
        instance.sendEmailVerification(this);
    }

    public void delete() throws Exception{
        instance.delete(this.getIdToken());
    }
}
