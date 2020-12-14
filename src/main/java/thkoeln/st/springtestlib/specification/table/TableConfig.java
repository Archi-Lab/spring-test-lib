package thkoeln.st.springtestlib.specification.table;

public class TableConfig {
    private String[] explanationDimensions;
    private String[] validRowValues;
    private String[] validColumnValues;
    private String[] validCellValues;


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
}
