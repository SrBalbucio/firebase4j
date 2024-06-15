package balbucio.org.firebase4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetails {

    private String displayName;
    private String photoUrl;
}
