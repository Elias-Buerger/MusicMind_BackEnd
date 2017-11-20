import com.musicmindproject.backend.rest.BackEndEndPoint;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class UnitTests {
    private BackEndEndPoint endPoint = new BackEndEndPoint();

    @Test
    public void testSendingEmail(){
        try {
            endPoint.doMailPost("king", "pethaudi@yahoo.de", "bürgi bae", "bürgi du bimst 1 bae und lot of love am haven");
        }
        catch(Exception e){
            fail();
        }
        assertTrue("it didnt manage it", true);
    }
}
