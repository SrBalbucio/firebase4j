import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.model.User;
import balbucio.org.firebase4j.persistent.FilePersistent;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.concurrent.Executors;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilePersistentTest {

    private FirebaseOptions options;
    private FirebaseAuth auth;
    private User createdUser;

    @BeforeAll
    public void create() throws Exception {
        options = FirebaseOptions.fromJsonFile(new File("test-credentials.json"));
        options.setPersistent(new FilePersistent(new File("firebase.persistent"), Executors.newCachedThreadPool()));
        auth = FirebaseAuth.newInstance(options);
    }

    @Test
    @DisplayName("Criar e salvar conta")
    @Order(0)
    public void createAndSave() throws Exception {
        createdUser = auth.signInAnonymously();
    }

    @Test
    @DisplayName("Checar se a conta foi salva")
    @Order(1)
    public void check() throws Exception {
        assertNotNull(auth.getCurrentUser());
    }

    @Test
    @DisplayName("Checar em uma instância diferente")
    @Order(2)
    public void recreate() throws Exception {
        FirebaseAuth auth2 = FirebaseAuth.newInstance(options);
        assertNotNull(auth2.getCurrentUser());
        assertTrue(auth2.getCurrentUser().getLocalId().equalsIgnoreCase(createdUser.getLocalId()));
    }

    @BeforeAll
    public void deleteUser() throws Exception {
        if (createdUser != null) {
            createdUser.delete();
        }
        options.getPersistent().clear();
    }

}
