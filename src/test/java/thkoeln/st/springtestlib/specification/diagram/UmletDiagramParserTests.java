package thkoeln.st.springtestlib.specification.diagram;

import org.junit.Test;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

public class UmletDiagramParserTests {

    @Test
    public void testDiagramParsing() throws Exception {
        GenericDiagramSpecificationTests genericDiagramSpecificationTests = new GenericDiagramSpecificationTests();
        genericDiagramSpecificationTests.testDiagram("class-diagram-solution.uxf", "class-diagram-w.uxf", DiagramType.CLASS_DIAGRAM);
    }
}
