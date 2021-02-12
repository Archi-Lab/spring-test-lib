package thkoeln.st.springtestlib.specification.diagram.elements.implementations;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

public class ClassElement extends RectangularElement {

    public ClassElement() {
        super(ElementType.CLASS);
    }

    public ClassElement(Point topLeft, float width, float height) {
        super(ElementType.CLASS, topLeft, width, height);
    }
}
