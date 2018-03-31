import com.musicmindproject.backend.DatabaseManager;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DbTest {

    private static DatabaseManager manager;

    @BeforeClass
    public static void init(){
            manager = DatabaseManager.getInstance();
    }

    @Test
    public void getStoredItems(){
        String testDe = manager.getQuestion(1, "de");
        String testEn = manager.getQuestion(1, "en");
        String testOther = manager.getQuestion(1, "boi");

        assertThat(testOther, is(testEn));
        assertThat(testDe, not(testEn));

        assertThat(manager.getQuestion(9999, "en"), is("Question does not exist..."));
    }
}
