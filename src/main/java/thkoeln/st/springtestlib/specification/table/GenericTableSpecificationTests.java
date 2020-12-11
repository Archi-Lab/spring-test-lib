package thkoeln.st.springtestlib.specification.table;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenericTableSpecificationTests {


    private Table loadTable(String path, TableType tableType) throws Exception {
        List<String> fileLines = loadFileLines(path);
        Table table = createTable(tableType);
        table.parseTable(fileLines);
        return table;
    }

    private List<String> loadFileLines(String path) throws Exception {
        List<String> fileLines = new ArrayList<>();

        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = br.readLine()) != null) {
            fileLines.add(line);
        }

        return fileLines;
    }


    public void testTableSpecification(String expectedPath, String actualPath, TableType tableType) throws Exception {
        Table expectedTable = loadTable(expectedPath, tableType);
        Table actualTable = loadTable(actualPath, tableType);

        if (!expectedTable.equals(actualTable)) {
            throw new Exception("Actual table does not match the expected table");
        }
    }

    private Table createTable(TableType tableType) {
        switch (tableType) {
            case ROWS_AND_COLUMNS:
                return new RowsAndColumnsTable();
            case ORDERED_COLUMNS:
                return new OrderedColumnsTable();
            case UNORDERED_COLUMNS:
                return new UnorderedColumnsTable();
            default:
                throw new IllegalArgumentException("This table type does not exist");
        }
    }
}
