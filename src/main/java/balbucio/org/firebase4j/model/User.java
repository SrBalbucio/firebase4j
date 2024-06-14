package balbucio.org.firebase4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    private String idToken;
    private String refreshToken;
    private long expiresIn;
    private String localId;
    private String email;
    private boolean emailVerified;
}
