import com.musicmindproject.backend.servlets.ContactEndPoint;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class UnitTests {
    ContactEndPoint endPoint = new ContactEndPoint();

    @Test
    public void testSendingEmail(){
        try {
            endPoint.doPost("king", "pethaudi@yahoo.de", "bürgi bae", "bürgi du bimst 1 bae und lot of love am haven");
        }
        catch(Exception e){
            fail();
        }
        assertTrue("it didnt manage it", true);
    }
}
