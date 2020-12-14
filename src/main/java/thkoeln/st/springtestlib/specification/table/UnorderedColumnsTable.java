package thkoeln.st.springtestlib.specification.table;

import java.util.ArrayList;
import java.util.List;

public class UnorderedColumnsTable extends Table {


    public UnorderedColumnsTable(TableConfig tableConfig) {
        super(TableType.UNORDERED_COLUMNS, tableConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnorderedColumnsTable)) {
            return false;
        }

        UnorderedColumnsTable otherTable = (UnorderedColumnsTable)obj;
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                return false;
            }

            List<Cell> nonEmptyCells = getAllNonEmptyCellsInColumn(c);
            List<Cell> otherTableNonEmptyCells = otherTable.getAllNonEmptyCellsInColumn(otherTableColumnIndex);
            if (isDimensionExplanation(columns.get(c))) {
                if (nonEmptyCells.size() < getRowCount() || otherTableNonEmptyCells.size() < otherTable.getRowCount()) {
                    return false;
                }
            } else {
                if (nonEmptyCells.size() != otherTableNonEmptyCells.size()) {
                    return false;
                }

                for (Cell nonEmptyCell : nonEmptyCells) {
                    if (!otherTableNonEmptyCells.contains(nonEmptyCell)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private List<Cell> getAllNonEmptyCellsInColumn(int column) {
        if (column < 0 || column >= getColumnCount()) {
            return new ArrayList<>();
        }

        List<Cell> allCells = new ArrayList<>();
        for (int r = 0; r < getRowCount(); r++) {
            Cell cell = getCell(r, column);
            if (cell != null && !cell.isEmpty()) {
                allCells.add(cell);
            }
        }

        return allCells;
    }
}
