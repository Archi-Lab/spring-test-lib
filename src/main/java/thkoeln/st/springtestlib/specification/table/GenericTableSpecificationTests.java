package thkoeln.st.springtestlib.specification.table;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenericTableSpecificationTests {


    private Table loadTable(String path, boolean summarizeRows) throws Exception {
        List<String> fileLines = loadFileLines(path);
        Table table = Table.parseTable(fileLines);
        if (summarizeRows) {
            table.summarizeRows();
        }
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


    public void testTableSpecification(String expectedPath, String actualPath, boolean summarizeRows) throws Exception {
        Table expectedTable = loadTable(expectedPath, summarizeRows);
        Table actualTable = loadTable(actualPath, summarizeRows);

        if (!expectedTable.equals(actualTable)) {
            throw new Exception("Actual table does not match the expected table");
        }
    }
}
