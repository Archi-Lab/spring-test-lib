package thkoeln.st.springtestlib.specification.diagram;

public class DiagramConfig {

    private boolean summarizeExceptions;


    public DiagramConfig(boolean summarizeExceptions) {
        this.summarizeExceptions = summarizeExceptions;
    }

    public boolean isSummarizeExceptions() {
        return summarizeExceptions;
    }

    public void setSummarizeExceptions(boolean summarizeExceptions) {
        this.summarizeExceptions = summarizeExceptions;
    }
}
