package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.Table;
import thkoeln.st.springtestlib.specification.table.TableConfig;
import thkoeln.st.springtestlib.specification.table.TableMismatchCause;
import thkoeln.st.springtestlib.specification.table.TableType;


public class SequencedOnlyColumnsTable extends Table {


    public SequencedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.SEQUENCED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof SequencedOnlyColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        SequencedOnlyColumnsTable otherTable = (SequencedOnlyColumnsTable)actualTable;
        otherTable.findDuplicateEntries();

        for (int otherTableR = 0; otherTableR < otherTable.getRowCount(); otherTableR++) {
            boolean foundMatchingRow = false;
            for (int r = 0; r < getRowCount(); r++) {
                if (compareSequenceRows(r, otherTableR, otherTable)) {
                    foundMatchingRow = true;
                    break;
                }
            }
            if (!foundMatchingRow) {
                tablesMismatch(Integer.toString(otherTableR+1), null, TableMismatchCause.CELL_MISMATCH);
            }
        }

        checkRowCountMatch(otherTable);
    }

    private void findDuplicateEntries() {
        for (int r = 0; r < getRowCount(); r++) {
            for (int i = 0; i < getRowCount(); i++) {
                if (r != i && compareSequenceRows(r, i, this)) {
                    tablesMismatch(Integer.toString(i+1), null, TableMismatchCause.DUPLICATE_ENTRIES);
                }
            }
        }
    }

    private boolean compareSequenceRows(int row, int otherTableRow, SequencedOnlyColumnsTable otherTable) {
        for (int c = 0; c < getColumnCount(); c++) {
            int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
            if (otherTableColumnIndex == -1) {
                tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
            }

            if (isDimensionExplanation(columns.get(c))) {
                if (getCell(row, c).isEmpty() || otherTable.getCell(otherTableRow, otherTableColumnIndex).isEmpty()) {
                    tablesMismatch(null, columns.get(c), TableMismatchCause.MISSING_EXPLANATION);
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
