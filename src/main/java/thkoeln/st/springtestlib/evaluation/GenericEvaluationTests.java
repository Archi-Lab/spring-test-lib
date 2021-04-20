package thkoeln.st.springtestlib.evaluation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide Tests to show results of the evaluation file
 */
public class GenericEvaluationTests {

    private static final String EVALUATION_FILE_PATH = "evaluation.md";
    private static final String EVALUATION_PASSED_SYMBOL = "*";
    private static final String EVALUATION_EXERCISE_SYMBOL = "#";


    private Map<String, EvaluationEntry> evaluationEntries;

    public GenericEvaluationTests() {
        this.evaluationEntries = loadEvaluations();
    }

    private Map<String, EvaluationEntry> loadEvaluations() {
        String totalEvaluationContent;
        try {
            totalEvaluationContent = getEvaluationFileContent();
        } catch (IOException e) {
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
        String[] totalSplitEvaluationContent = totalEvaluationContent.split("\n");

        Map<String, EvaluationEntry> evaluationEntries = new LinkedHashMap<>();
        List<String> evaluationContent = new ArrayList<>();
        boolean foundAtLeastOneEntry = false;

        for (String contentLine : totalSplitEvaluationContent) {
            if (contentLine.contains(EVALUATION_EXERCISE_SYMBOL)) {
                if (foundAtLeastOneEntry) {
                    addEvaluationEntry(evaluationEntries, evaluationContent);
                    evaluationContent = new ArrayList<>();
                }
                foundAtLeastOneEntry = true;
            }
            if (foundAtLeastOneEntry) {
                evaluationContent.add(contentLine);
            }
        }

        if (foundAtLeastOneEntry) {
            addEvaluationEntry(evaluationEntries, evaluationContent);
        }

        return evaluationEntries;
    }

    private void addEvaluationEntry(Map<String, EvaluationEntry> evaluationEntries, List<String> evaluationLines) {
        ensureOneEmptyLineAtTheEnd(evaluationLines);

        String evaluationKey = evaluationLines.get(0);
        boolean passed = evaluationKey.contains(EVALUATION_PASSED_SYMBOL);
        evaluationKey = evaluationKey.replace(EVALUATION_PASSED_SYMBOL, "").replace(EVALUATION_EXERCISE_SYMBOL, "").trim();

        String explanation = "";
        for (int i = 1; i < evaluationLines.size(); i++) {
            explanation += evaluationLines.get(i);

            if (i < evaluationLines.size() - 1) {
                explanation += "\n";
            }
        }

        EvaluationEntry evaluationEntry = new EvaluationEntry(explanation, passed);
        evaluationEntries.put(evaluationKey, evaluationEntry);
    }

    private void ensureOneEmptyLineAtTheEnd(List<String> evaluationLines) {
        for (int i = evaluationLines.size() - 1; i >= 0; i--) {
            if (evaluationLines.get(i).trim().length() > 0) {
                break;
            }
            evaluationLines.remove(i);
        }
        evaluationLines.add("");
    }

    private String getEvaluationFileContent() throws IOException {
        File file = new File(getClass().getClassLoader().getResource(EVALUATION_FILE_PATH).getFile());
        return new String(Files.readAllBytes(file.toPath()));
    }

    public void evaluateExercise(String exerciseKey) throws Exception {
        EvaluationEntry foundEvaluationEntry = evaluationEntries.get(exerciseKey);
        if (foundEvaluationEntry == null) {
            throw new Exception("Could not find evaluation for exercise " + exerciseKey);
        }

        if (!foundEvaluationEntry.getPassed()) {
            throw new Exception(foundEvaluationEntry.getExplanation());
        }
    }
}
