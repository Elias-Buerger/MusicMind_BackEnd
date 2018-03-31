import com.musicmindproject.backend.logic.PersonalityEvaluator;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PersonalityEvaluatorTest {
    @Test
    public void getInstance() throws Exception {
        PersonalityEvaluator ev = PersonalityEvaluator.getInstance();
        PersonalityEvaluator ev2 = PersonalityEvaluator.getInstance();
        assertEquals(ev, ev2);
    }

    @Test
    public void getOutputs() throws Exception {
        PersonalityEvaluator ev = PersonalityEvaluator.getInstance();
        double[] inputs = new double[30];
        for (int i = 0; i < inputs.length; i++){
            inputs[i] = i%5;
        }
        double[] outputs = {
                41.87979539641944,
                43.02721088435374,
                47.16713881019831,
                44.3029490616622,
                31.36986301369863,
        };
        assertArrayEquals(ev.getOutputs(inputs), outputs, 0.0001);
    }

}