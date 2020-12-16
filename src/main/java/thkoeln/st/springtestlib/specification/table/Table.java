package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public abstract class Table {

    protected TableType tableType;

    protected List<String> rows = new ArrayList<>();
    protected List<String> columns = new ArrayList<>();
    protected List<List<Cell>> cells = new ArrayList<>();

    protected TableConfig tableConfig;


    public Table(TableType tableType, TableConfig tableConfig) {
        this.tableType = tableType;

        this.tableConfig = tableConfig;
    }

    public void addRow(String rowName) {
        if (!isRowValid(rowName)) {
            throw new InputMismatchException(rowName + " is not a valid row name");
        }

        List<Cell> newRowArray = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            newRowArray.add(null);
        }
        cells.add(newRowArray);
        rows.add(rowName);
    }

    public void addColumn(String columnName) {
        if (!isColumnValid(columnName)) {
            throw new InputMismatchException(columnName + " is not a valid column name");
        }

        for (List<Cell> row : cells) {
            row.add(null);
        }
        columns.add(columnName);
    }

    public boolean isRowValid(String rowName) {
        if (tableConfig.getValidRowValues().length == 0) {
            return true;
        }

        for (String s : tableConfig.getValidRowValues()) {
            if (s.equalsIgnoreCase(rowName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isColumnValid(String columnName) {
        if (tableConfig.getValidColumnValues().length == 0) {
            return true;
        }

        for (String s : tableConfig.getValidColumnValues()) {
            if (s.equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDimensionExplanation(String dimensionName) {
        for (String s : tableConfig.getExplanationDimensions()) {
            if (s.equalsIgnoreCase(dimensionName)) {
                return true;
            }
        }
        return false;
    }

    protected int getRowIndex(String rowName) {
        for (int i = 0; i < rows.size(); i++) {
            if (rowName.equalsIgnoreCase(rows.get(i))) {
                return i;
            }
        }
        return -1;
    }

    protected int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.size(); i++) {
            if (columnName.equalsIgnoreCase(columns.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void setCell(int row, int column, Cell cell) {
        cells.get(row).set(column, cell);
    }

    public Cell getCell(int row, int column) {
        if (row < 0 || column < 0 || row >= cells.size() || column >= cells.get(row).size()) {
            return null;
        }

        return cells.get(row).get(column);
    }

    public Cell getCell(int row, String columnName) {
        int column = getColumnIndex(columnName);
        return getCell(row, column);
    }

    public Cell getCell(String rowName, String columnName) {
        int row = getRowIndex(rowName);
        int column = getColumnIndex(columnName);
        return getCell(row, column);
    }

    protected String[] parseElementsInContentLine(String contentLine) {
        String[] elements = contentLine.trim().split("\\|");
        int columnMarks = (int)contentLine.chars().filter(ch -> ch == '|').count();

        String[] filteredElements = new String[columnMarks - 1];
        for (int i = 1; i < columnMarks; i++) {
            filteredElements[i-1] = elements[i].trim();
        }
        return filteredElements;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columns.size();
    }

    protected List<String> filterContentLines(List<String> contentLines) {
        List<String> filteredContentLines = new ArrayList<>();

        for (String contentLine : contentLines) {
            if (contentLine.contains("|")) {
                filteredContentLines.add(contentLine);
            }
        }

        return filteredContentLines;
    }

    protected List<String> testSyntax(List<String> contentLines) {
        if (contentLines.size() < 3) {
            throw new InputMismatchException("A table consists of at least 3 lines");
        }

        long expectedColumnMarks = contentLines.get(0).chars().filter(ch -> ch == '|').count();
        if (expectedColumnMarks < 2) {
            throw new InputMismatchException("A table line needs at least two \"|\" chars");
        }

        for (String contentLine : contentLines) {
            long columnMarks = contentLine.chars().filter(ch -> ch == '|').count();
            if (expectedColumnMarks != columnMarks) {
                throw new InputMismatchException("Each table line needs the same number of \"|\" chars");
            }
        }

        String[] split = parseElementsInContentLine(contentLines.get(1));
        for (int i = 1; i < split.length; i++) {
            long strokes = split[i].chars().filter(ch -> ch == '-').count();
            if (strokes < 3) {
                throw new InputMismatchException("Each column in the second line needs at least 3 \"-\" chars");
            }
        }

        return contentLines;
    }

    protected String[] getValidCellValues(int row, int column) {
        return isDimensionExplanation(rows.get(row)) || isDimensionExplanation(columns.get(column)) ? new String[]{} : tableConfig.getValidCellValues();
    }

    public void parse(List<String> contentLines) {
        contentLines = testSyntax(filterContentLines(contentLines));

        String[] columnNames = parseElementsInContentLine(contentLines.get(0));
        for (String columnName : columnNames) {
            addColumn(columnName.trim());
        }

        for (int i = 2; i < contentLines.size(); i++) {
            addRow(null);
            String[] columns = parseElementsInContentLine(contentLines.get(i));
            for (int j = 0; j < columns.length; j++) {
                setCell(i-2, j, Cell.parseCell(columns[j], getValidCellValues(i-2, j)));
            }
        }
    }
}
