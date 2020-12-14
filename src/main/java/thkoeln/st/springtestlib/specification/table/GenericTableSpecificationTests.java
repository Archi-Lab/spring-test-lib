package thkoeln.st.springtestlib.specification.table;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenericTableSpecificationTests {

    private ObjectMapper objectMapper = new ObjectMapper();


    private Table loadTable(String path, TableType tableType, TableConfig tableConfig) throws Exception {
        List<String> fileLines = loadFileLines(path);
        Table table = createTable(tableType, tableConfig);
        table.parse(fileLines);
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

    private TableConfig loadTableConfig(String tableConfigPath) throws IOException {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(tableConfigPath);

        return objectMapper.readValue(new InputStreamReader(inputStream), TableConfig.class);
    }

    public void testTableSpecification(String expectedPath, String actualPath, String tableConfigPath, TableType tableType) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);

        Table expectedTable = loadTable(expectedPath, tableType, tableConfig);
        Table actualTable = loadTable(actualPath, tableType, tableConfig);

        if (!expectedTable.equals(actualTable)) {
            throw new Exception("Actual table does not match the expected table");
        }
    }

    public void testTableSyntax(String actualPath, String tableConfigPath, TableType tableType) throws Exception {
        TableConfig tableConfig = loadTableConfig(tableConfigPath);
        loadTable(actualPath, tableType, tableConfig);
    }

    private Table createTable(TableType tableType, TableConfig tableConfig) {
        switch (tableType) {
            case ROWS_AND_COLUMNS:
                return new RowsAndColumnsTable(tableConfig);
            case ORDERED_ONLY_COLUMNS:
                return new OrderedOnlyColumnsTable(tableConfig);
            case UNORDERED_ONLY_COLUMNS:
                return new UnorderedOnlyColumnsTable(tableConfig);
            case SEQUENCED_ONLY_COLUMNS:
                return new SequencedOnlyColumnsTable(tableConfig);
            default:
                throw new IllegalArgumentException("This table type does not exist");
        }
    }
}
