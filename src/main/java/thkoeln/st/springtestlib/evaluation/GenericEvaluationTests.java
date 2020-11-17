package thkoeln.st.springtestlib.evaluation;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide Tests to show results of the evaluation file
 */
public class GenericEvaluationTests {

    private static final String EVALUATION_FILE_PATH = "evaluation.yaml";


    private Map<String, EvaluationEntry> evaluationEntries;

    public GenericEvaluationTests() {
        this.evaluationEntries = loadEvaluations();
    }

    private Map<String, EvaluationEntry> loadEvaluations() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(EVALUATION_FILE_PATH);
        Map<Object, Map> obj = yaml.load(inputStream);

        LinkedHashMap<String, EvaluationEntry> evaluationEntries = new LinkedHashMap<>();
        for (Map.Entry<Object, Map> entry : obj.entrySet()) {
            String explanation = (String)entry.getValue().get("explanation");

            Integer points = (Integer)(entry.getValue().get("points"));
            Integer maxPoints = (Integer)(entry.getValue().get("maxPoints"));
            Integer attempts = (Integer)(entry.getValue().get("attempts"));
            Integer maxAttempts = (Integer)(entry.getValue().get("maxAttempts"));
            Boolean passed = (Boolean)(entry.getValue().get("passed"));
            String correctedBy = (String)(entry.getValue().get("correctedBy"));

            EvaluationEntry evaluationEntry = new EvaluationEntry(explanation, points, maxPoints, attempts, maxAttempts, passed, correctedBy);
            evaluationEntries.put(entry.getKey().toString(), evaluationEntry);
        }

        return evaluationEntries;
    }

    public void evaluateExercise(String exerciseKey) throws Exception {
        EvaluationEntry foundEvaluationEntry = evaluationEntries.get(exerciseKey);
        if (foundEvaluationEntry == null) {
            throw new Exception("Could not find evaluation for exercise " + exerciseKey);
        }

        if (!foundEvaluationEntry.getPassed()) {
            StringBuilder sb = new StringBuilder();
            sb.append(foundEvaluationEntry.getExplanation());

            if (foundEvaluationEntry.getPoints() != null && foundEvaluationEntry.getMaxPoints() != null) {
                sb.append(" - Points: ");
                sb.append(foundEvaluationEntry.getPoints());
                sb.append("/");
                sb.append(foundEvaluationEntry.getMaxPoints());
            }

            if (foundEvaluationEntry.getAttempts() != null && foundEvaluationEntry.getMaxAttempts() != null) {
                sb.append(" - Attempt: ");
                sb.append(foundEvaluationEntry.getAttempts());
                sb.append("/");
                sb.append(foundEvaluationEntry.getMaxAttempts());
            }

            throw new Exception(sb.toString());
        }
    }
}
