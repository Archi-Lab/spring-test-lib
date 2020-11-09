package thkoeln.st.springtestlib.evaluation;

public class EvaluationEntry {

    private String explanation;
    private Integer points;
    private Integer maxPoints;
    private Integer attempts;
    private Integer maxAttempts;
    private Boolean passed;
    private String correctedBy;


    public EvaluationEntry(String explanation, Integer points, Integer maxPoints, Integer attempts, Integer maxAttempts, Boolean passed, String correctedBy) {
        this.explanation = explanation;
        this.points = points;
        this.maxPoints = maxPoints;
        this.attempts = attempts;
        this.maxAttempts = maxAttempts;
        this.passed = passed;
        this.correctedBy = correctedBy;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(Integer maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public String getCorrectedBy() {
        return correctedBy;
    }

    public void setCorrectedBy(String correctedBy) {
        this.correctedBy = correctedBy;
    }
}