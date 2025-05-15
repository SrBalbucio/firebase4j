import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.FirebaseServerAppCheck;
import balbucio.org.firebase4j.model.AppCheckToken;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerAppCheckTest {

    private FirebaseOptions options;
    private FirebaseServerAppCheck serverAppCheck;

    @BeforeAll
    public void beforeAll() throws Exception {
        this.options = FirebaseOptions.fromJsonFile(new File("test-credentials.json"))
                .withServiceAccount(new File("service-account.json"));
        this.serverAppCheck = FirebaseServerAppCheck.newInstance(options);
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
