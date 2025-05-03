package balbucio.org.firebase4j;

import balbucio.org.firebase4j.impl.appCheck.ServerAppCheckV1;
import balbucio.org.firebase4j.model.AppCheckToken;
import lombok.NonNull;

import java.io.IOException;
import java.util.Optional;

public abstract class FirebaseServerAppCheck {

    public static FirebaseServerAppCheck newInstance(@NonNull FirebaseOptions options) {
        return new ServerAppCheckV1(options);
    }

    protected FirebaseOptions options;

    public FirebaseServerAppCheck(FirebaseOptions options) {
        this.options = options;
    }

    public abstract String getJWTToken();
    public abstract Optional<AppCheckToken> createToken(boolean limitedUse) throws IOException;
}
