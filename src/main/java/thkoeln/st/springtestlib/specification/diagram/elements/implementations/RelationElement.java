package thkoeln.st.springtestlib.specification.diagram.elements.implementations;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.LineElement;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;

public class RelationElement extends LineElement {

    public RelationElement() {
        super(ElementType.RELATION);
    }

    public RelationElement(Point start, Point end) {
        super(ElementType.RELATION, start, end);
    }
}
