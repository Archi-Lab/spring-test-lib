package thkoeln.st.springtestlib.specification.table.implementations;

import thkoeln.st.springtestlib.specification.table.Table;
import thkoeln.st.springtestlib.specification.table.TableConfig;
import thkoeln.st.springtestlib.specification.table.TableMismatchCause;
import thkoeln.st.springtestlib.specification.table.TableType;


public class OrderedOnlyColumnsTable extends Table {


    public OrderedOnlyColumnsTable(TableConfig tableConfig) {
        super(TableType.ORDERED_ONLY_COLUMNS, tableConfig);
    }

    @Override
    public void compareToActualTable(Table actualTable) {
        if (!(actualTable instanceof OrderedOnlyColumnsTable)) {
            throw new IllegalArgumentException("Wrong table type passed as parameter");
        }

        OrderedOnlyColumnsTable otherTable = (OrderedOnlyColumnsTable)actualTable;
        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
                if (otherTableColumnIndex == -1) {
                    tablesMismatch(TableMismatchCause.COLUMN_NOT_FOUND);
                }

                if (isDimensionExplanation(columns.get(c))) {
                    if (getCell(r, c).isEmpty() || otherTable.getCell(r, otherTableColumnIndex).isEmpty()) {
                        tablesMismatch(Integer.toString(r+1), columns.get(c), TableMismatchCause.MISSING_EXPLANATION);
                    }
                } else {
                    if (!getCell(r, c).equals(otherTable.getCell(r, otherTableColumnIndex))) {
                        tablesMismatch(Integer.toString(r+1), columns.get(c), TableMismatchCause.CELL_MISMATCH);
                    }
                }
            }
        }

        checkRowCountMatch(otherTable);
    }
}
