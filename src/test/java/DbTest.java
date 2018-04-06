import com.musicmindproject.backend.logic.QuestionManager;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class DbTest {
    @Inject
    private QuestionManager manager;

    @Test
    public void getStoredItems(){
        String testDe = manager.retrieve(1, "de");
        String testEn = manager.retrieve(1, "en");
        String testOther = manager.retrieve(1, "boi");

        assertThat(testOther, is(testEn));
        assertThat(testDe, not(testEn));

        assertThat(manager.retrieve(9999, "en"), is("Question does not exist..."));
    }
}
