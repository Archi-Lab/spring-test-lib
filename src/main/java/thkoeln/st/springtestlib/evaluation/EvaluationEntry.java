package thkoeln.st.springtestlib.evaluation;

public class EvaluationEntry {

    private String explanation;
    private Boolean passed;


    public EvaluationEntry(String explanation, Boolean passed) {
        this.explanation = explanation;
        this.passed = passed;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }
}