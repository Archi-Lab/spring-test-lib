package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;

public class UmlClassParser implements ElementParser<UmletElement> {

    @Override
    public ClassElement parseElement(UmletElement sourceElement) {
        ClassElement classElement = new ClassElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());
        classElement.setId(sourceElement.getPanelAttributes());

        return classElement;
    }
}
