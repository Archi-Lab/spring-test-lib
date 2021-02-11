package thkoeln.st.springtestlib.specification.diagram.parser;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.implementations.DiagramType;

import java.io.IOException;

public abstract class DiagramParser {

    public abstract Diagram parseDiagram(String path, DiagramType diagramType) throws IOException;

    protected Diagram createDiagram(DiagramType diagramType) {
        try {
            return (Diagram) diagramType.getDiagramClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Invalid diagram type", e);
        }
    }
}
