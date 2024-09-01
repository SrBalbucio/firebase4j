import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.Firestore;
import balbucio.org.firebase4j.model.DocumentSnapshot;
import balbucio.org.firebase4j.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirestoreTest {

    private FirebaseOptions options;
    private FirebaseAuth auth;
    private Firestore firestore;
    private User createdUser;
    private DocumentSnapshot snapshot;

    @BeforeAll
    @SneakyThrows
    public void init() {
        options = FirebaseOptions.fromJsonFile(new File("test-credentials.json"));
        auth = FirebaseAuth.newInstance(options);
        firestore = Firestore.newInstance(options, "(default)", auth);
        createdUser = auth.signInAnonymously();
    }

    @Test
    @DisplayName("Get Document")
    @Order(0)
    public void getDocument() throws Exception {
        snapshot = firestore.getDocument("users", "srbalbucio");
        assertEquals("Joao Gabriel", snapshot.asString("name"));
    }

    @Test
    @DisplayName("Update Document")
    @Order(1)
    public void updateDocument() throws Exception {
        firestore.updateDocument(snapshot.put("name", "Alberto"));
        assertEquals("Alberto", snapshot.asString("name"));
    }

    @Test
    @DisplayName("Deletar as contas criadas")
    @Order(99)
    public void deleteUsers() throws Exception {
        auth.logout();
        if (createdUser != null) {
            createdUser.delete();
        }
    }

}
