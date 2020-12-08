package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private List<List<Cell>> cells = new ArrayList<>();


    private void addRow() {
        cells.add(new ArrayList<>());
    }

    public Cell addCell(int row, Cell cell) {
        while (cells.size() < (row + 1)) addRow();

        List<Cell> columns = cells.get(row);
        columns.add(cell);
        return cell;
    }

    public Cell getCell(int row, int column) {
        if (row >= cells.size() || column >= cells.get(row).size()) {
            return null;
        }

        return cells.get(row).get(column);
    }

    public void summarizeRows() {
        if (cells.isEmpty()) {
            return;
        }

        for (int i = 1; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                getCell(0, j).addContent(getCell(i, j));
            }
        }

        for (int i = cells.size() - 1; i >= 1; i--) {
            cells.remove(i);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Table)) {
            return false;
        }

        Table otherTable = (Table)obj;

        for (int row = 0; row < cells.size(); row++) {
            for (int column = 0; column < cells.get(row).size(); column++) {
                if (!getCell(row, column).isEmpty()
                    && !getCell(row, column).equals(otherTable.getCell(row, column))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Table parseTable(List<String> fileLines) {
        Table table = new Table();
        for (int i = 2; i < fileLines.size(); i++) {
            String[] columns = fileLines.get(i).trim().split("\\|");
            for (int j = 1; j < columns.length; j++) {
                table.addCell(i-2, Cell.parseCell(columns[j].trim()));
            }
        }
        return table;
    }
}
