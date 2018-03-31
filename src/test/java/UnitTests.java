import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.fail;

public class UnitTests {
    /*@Test
    public void testSendingEmail(){
        try {
            endPoint.doMailPost("king", "pethaudi@yahoo.de", "bürgi bae", "bürgi du bimst 1 bae und lot of love am haven");
        }
        catch(Exception e){
            fail();
        }
        assertTrue("it didnt manage it", true);
    }*/

    @Test
    public void testSendingEmailViaWebtarget(){
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://www.musicmindproject.com:8080/backend/rest/contact");
        postMethod.addParameter("name", "karlmarx");
        postMethod.addParameter("email", "karl@marx.int");
        postMethod.addParameter("subject", "communism");
        postMethod.addParameter("comment", "communism will win");
        try {
            httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (postMethod.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                String resp = postMethod.getResponseBodyAsString();
            } else {
                System.out.println(postMethod.getStatusLine());
                fail();
            }
        }
        catch(IOException ex){
            fail();
        }
    }
}
