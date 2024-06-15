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
}
