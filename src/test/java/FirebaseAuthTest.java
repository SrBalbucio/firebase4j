import balbucio.org.firebase4j.FirebaseAuth;
import balbucio.org.firebase4j.FirebaseOptions;
import balbucio.org.firebase4j.impl.auth.AuthV1;
import balbucio.org.firebase4j.model.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirebaseAuthTest {

    private FirebaseOptions options;
    private FirebaseAuth auth;
    private List<User> createdUsers = new ArrayList<>();
    private User createdUser;

    @BeforeAll
    @SneakyThrows
    public void init(){
        options = FirebaseOptions.fromJsonFile(new File("test-credentials.json"));
        auth = FirebaseAuth.newInstance(options);
    }

    @Test
    @DisplayName("Criar conta anonima")
    @Order(0)
    public void anonymously() throws Exception{
        User user = auth.signInAnonymously();
        System.out.println(user);
        assertNotNull(user);
        createdUsers.add(user);
        auth.logout();
    }

    @Test
    @DisplayName("Criar conta com email e senha")
    @Order(1)
    public void register() throws Exception{
        User user = auth.signUp(options.getEmailTest(), "secure69password");
        System.out.println(user);
        assertNotNull(user);
        createdUsers.add(user);
        createdUser = user;
        auth.logout();
    }

    @Test
    @DisplayName("Autenticar com email e senha")
    @Order(2)
    public void authenticate() throws Exception{
        User user = auth.signIn(options.getEmailTest(), "secure69password");
        System.out.println(user);
        assertNotNull(user);
        assertEquals(createdUser.getLocalId(), user.getLocalId());
    }

    @Test
    @DisplayName("Enviar email de verificação")
    @Order(3)
    public void sendEmailVerification() throws Exception{
        auth.sendEmailVerification(createdUser);
    }

    @Test
    @DisplayName("Atualizar dados da conta")
    @Order(4)
    public void update() throws Exception{
        createdUser.updateDetails("Balbucio", "https://ead.ifsul.edu.br/images/phocagallery/galeria2/thumbs/phoca_thumb_l_image03_grd.png");
    }

    @Test
    @DisplayName("Consultar dados da conta")
    @Order(5)
    public void lookup() throws Exception{
        auth.getUserDetails(createdUser);
        assertEquals("Balbucio", createdUser.getDetails().getDisplayName());
        assertEquals("https://ead.ifsul.edu.br/images/phocagallery/galeria2/thumbs/phoca_thumb_l_image03_grd.png", createdUser.getDetails().getPhotoUrl());
    }

    @Test
    @DisplayName("Deletar as contas criadas")
    @Order(99)
    public void deleteUsers() throws Exception{
        auth.logout();
        for (User user : createdUsers) {
            auth.deleteUser(user);
        }
    }
}
