import com.web.note.util.Security;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NormalTest {


    @Test
    public void testPassword(){
        String s = Security.passwordHash("example", "6bc1f9ec8d894008861fce39a8f29b6c");
        System.out.println(s);
    }
}
