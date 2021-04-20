package thkoeln.st.springtestlib.evaluation;

import org.junit.Test;

public class EvaluationTest {

    @Test
    public void testEvaluationForExercise1() throws Exception {
        GenericEvaluationTests genericEvaluationTests = new GenericEvaluationTests();
        genericEvaluationTests.evaluateExercise("Exercise 1");
    }

    @Test
    public void testEvaluationForExercise2() throws Exception {
        GenericEvaluationTests genericEvaluationTests = new GenericEvaluationTests();
        genericEvaluationTests.evaluateExercise("Exercise 2");
    }
}
