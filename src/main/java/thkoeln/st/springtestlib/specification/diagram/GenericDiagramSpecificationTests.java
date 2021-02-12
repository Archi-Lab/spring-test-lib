package thkoeln.st.springtestlib.specification.diagram;

import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmletDiagramParser;

import java.io.IOException;

public class GenericDiagramSpecificationTests {

    public void testDiagram(String expectedPath, String actualPath, DiagramType diagramType) throws IOException {
        Diagram expectedDiagram = loadDiagram(expectedPath, diagramType);
        Diagram actualDiagram = loadDiagram(actualPath, diagramType);

        //expectedDiagram.compareToActualTable(actualDiagram);
    }

    private Diagram loadDiagram(String path, DiagramType diagramType) throws IOException {
        UmletDiagramParser umletDiagramParser = new UmletDiagramParser();
        return umletDiagramParser.parseDiagram(path, diagramType);
    }
}
