package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

public class RelationPointer {

    private RectangularElement connectedElement;
    private RelationArrowType relationArrowType;
    private String cardinality;


    public RectangularElement getConnectedElement() {
        return connectedElement;
    }

    public void setConnectedElement(RectangularElement connectedElement) {
        this.connectedElement = connectedElement;
    }

    public RelationArrowType getRelationArrowType() {
        return relationArrowType;
    }

    public void setRelationArrowType(RelationArrowType relationArrowType) {
        this.relationArrowType = relationArrowType;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }
}
