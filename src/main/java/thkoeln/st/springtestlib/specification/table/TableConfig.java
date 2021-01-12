package thkoeln.st.springtestlib.specification.table;

public class TableConfig {
    private String[] explanationDimensions;
    private String[] validRowValues;
    private String[] validColumnValues;
    private String[] validCellValues;

    private boolean showRowHints;
    private boolean showColumnHints;


    public String[] getExplanationDimensions() {
        return explanationDimensions;
    }

    public void setExplanationDimensions(String[] explanationDimensions) {
        this.explanationDimensions = explanationDimensions;
    }

    public String[] getValidRowValues() {
        return validRowValues;
    }

    public void setValidRowValues(String[] validRowValues) {
        this.validRowValues = validRowValues;
    }

    public String[] getValidCellValues() {
        return validCellValues;
    }

    public void setValidCellValues(String[] validCellValues) {
        this.validCellValues = validCellValues;
    }

    public String[] getValidColumnValues() {
        return validColumnValues;
    }

    public void setValidColumnValues(String[] validColumnValues) {
        this.validColumnValues = validColumnValues;
    }

    public boolean isShowRowHints() {
        return showRowHints;
    }

    public void setShowRowHints(boolean showRowHints) {
        this.showRowHints = showRowHints;
    }

    public boolean isShowColumnHints() {
        return showColumnHints;
    }

    public void setShowColumnHints(boolean showColumnHints) {
        this.showColumnHints = showColumnHints;
    }
}
