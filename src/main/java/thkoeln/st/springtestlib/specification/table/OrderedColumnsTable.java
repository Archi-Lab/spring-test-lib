package thkoeln.st.springtestlib.specification.table;

public class OrderedColumnsTable extends Table {


    public OrderedColumnsTable(TableConfig tableConfig) {
        super(TableType.ORDERED_COLUMNS, tableConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OrderedColumnsTable)) {
            return false;
        }

        OrderedColumnsTable otherTable = (OrderedColumnsTable)obj;
        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                int otherTableColumnIndex = otherTable.getColumnIndex(columns.get(c));
                if (otherTableColumnIndex == -1) {
                    return false;
                }

                if (isDimensionExplanation(columns.get(c))) {
                    if (getCell(r, c).isEmpty() || otherTable.getCell(r, otherTableColumnIndex).isEmpty()) {
                        return false;
                    }
                } else {
                    if (!getCell(r, c).equals(otherTable.getCell(r, otherTableColumnIndex))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
