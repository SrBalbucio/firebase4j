package balbucio.org.firebase4j.server;

import balbucio.org.firebase4j.server.impl.ServerAppCheckV1;
import balbucio.org.firebase4j.server.model.AppCheckToken;
import lombok.NonNull;

import java.io.IOException;
import java.util.Optional;

public abstract class FirebaseServerAppCheck {

    public static FirebaseServerAppCheck newInstance(@NonNull FirebaseServerOptions options) {
        return new ServerAppCheckV1(options);
    }

    protected final FirebaseServerOptions options;

    public FirebaseServerAppCheck(@NonNull FirebaseServerOptions options) {
        this.options = options;
    }

    public abstract String getJWTToken();

    public abstract Optional<AppCheckToken> createToken(boolean limitedUse) throws IOException;
}
