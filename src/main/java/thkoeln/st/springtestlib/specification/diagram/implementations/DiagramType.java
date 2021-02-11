package thkoeln.st.springtestlib.specification.diagram.implementations;


public enum DiagramType {

    CLASS_DIAGRAM(ClassDiagram.class);

    private Class<?> diagramClass;


    DiagramType(Class<?> diagramClass) {
        this.diagramClass = diagramClass;
    }

    public Class<?> getDiagramClass() {
        return diagramClass;
    }
}
