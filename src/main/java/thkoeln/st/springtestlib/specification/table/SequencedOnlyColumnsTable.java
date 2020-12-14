package thkoeln.st.springtestlib.specification.table;

public class SequencedOnlyColumnsTable extends Table {


    public SequencedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.SEQUENCED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SequencedOnlyColumnsTable)) {
            return false;
        }

        SequencedOnlyColumnsTable otherTable = (SequencedOnlyColumnsTable)obj;
        if (getRowCount() != otherTable.getRowCount()) {
            return false;
        }

        for (int r = 0; r < getRowCount(); r++) {
            boolean foundMatchingRow = false;
            for (int otherTableR = 0; otherTableR < otherTable.getRowCount(); otherTableR++) {
                if (compareSequenceRows(r, otherTableR, otherTable)) {
                    foundMatchingRow = true;
                    break;
                }
            }
            if (!foundMatchingRow) {
                return false;
            }
        }
        return true;
    }

    private boolean compareSequenceRows(int row, int otherTableRow, SequencedOnlyColumnsTable otherTable) {
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                return false;
            }

            if (isDimensionExplanation(columns.get(c))) {
                if (getCell(row, c).isEmpty() || otherTable.getCell(otherTableRow, otherTableColumnIndex).isEmpty()) {
                    return false;
                }
            } else {
                if (!getCell(row, c).equals(otherTable.getCell(otherTableRow, otherTableColumnIndex))) {
                    return false;
                }
            }
        }
        return true;
    }
}
