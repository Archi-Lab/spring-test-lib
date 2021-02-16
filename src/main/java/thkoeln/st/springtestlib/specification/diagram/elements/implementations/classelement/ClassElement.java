package thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.RectangularElement;

import java.util.ArrayList;
import java.util.List;

public class ClassElement extends RectangularElement {

    private List<ClassAttribute> attributes = new ArrayList<>();

    public ClassElement() {
        super(ElementType.CLASS);
    }

    public ClassElement(Point topLeft, float width, float height) {
        super(ElementType.CLASS, topLeft, width, height);
    }

    public List<ClassAttribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(ClassAttribute attribute) {
        attributes.add(attribute);
    }
}
