package balbucio.org.firebase4j.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppCheckToken {

    private String token;
    private String ttl;
}
