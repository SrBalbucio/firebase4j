import java.io.File;

/**
 * Resolves paths like {@code test-credentials.json} from the module directory or the reactor root.
 */
public final class ProjectTestFiles {

    private ProjectTestFiles() {
    }

    public static File file(String relative) {
        File f = new File(relative);
        if (f.isFile()) {
            return f;
        }
        File parent = new File(System.getProperty("user.dir")).getParentFile();
        if (parent != null) {
            File p = new File(parent, relative);
            if (p.isFile()) {
                return p;
            }
        }
        return f;
    }
}
