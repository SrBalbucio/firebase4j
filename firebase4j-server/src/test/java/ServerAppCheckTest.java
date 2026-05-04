import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.server.FirebaseServerAppCheck;
import balbucio.org.firebase4j.server.FirebaseServerOptions;
import balbucio.org.firebase4j.server.model.AppCheckToken;
import org.junit.jupiter.api.*;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerAppCheckTest {

    private FirebaseServerOptions serverOptions;
    private FirebaseServerAppCheck serverAppCheck;

    @BeforeAll
    public void beforeAll() throws Exception {
        FirebaseOptions client = FirebaseOptions.fromJsonFile(ProjectTestFiles.file("test-credentials.json"));
        this.serverOptions = FirebaseServerOptions.fromClientAndServiceAccount(
                client,
                ProjectTestFiles.file("service-account.json"));
        this.serverAppCheck = FirebaseServerAppCheck.newInstance(serverOptions);
    }

    @Test
    @DisplayName("Create a new Token")
    @Order(0)
    public void createToken() throws Exception {
        Optional<AppCheckToken> token = serverAppCheck.createToken(false);
        Assertions.assertTrue(token.isPresent());
        System.out.println(token.get());
    }

}
