package thkoeln.st.springtestlib.specification.table;

public class OrderedColumnsTable extends Table {


    public OrderedColumnsTable() {
        super(TableType.ORDERED_COLUMNS);
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

                if (!getCell(r, c).equals(otherTable.getCell(r, otherTableColumnIndex))) {
                    return false;
                }
            }
        }

        return true;
    }
}
