package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.elements.Point;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassAttribute;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;

import java.util.ArrayList;
import java.util.List;

public class UmlClassParser extends UmletParser {

    @Override
    public ClassElement parseElement(UmletElement sourceElement) {
        ClassElement classElement = new ClassElement(
                new Point(sourceElement.getUmletCoordinates().getX(),
                sourceElement.getUmletCoordinates().getY()),
                sourceElement.getUmletCoordinates().getWidth(),
                sourceElement.getUmletCoordinates().getHeight());

        String[] properties = getSplittedProperties(sourceElement);
        classElement.setId(properties[0]);
        parseAttributes(classElement, properties);

        return classElement;
    }

    private void parseAttributes(ClassElement classElement, String[] properties) {
        List<String> attributeBody = getAttributeBody(properties);
        for (String attributeStr : attributeBody) {
            String[] attributeParts = attributeStr.split(":");
            if (attributeParts.length >= 2) {
                classElement.addAttribute(new ClassAttribute(attributeParts[0].trim(), attributeParts[1].trim()));
            } else {
                classElement.addAttribute(new ClassAttribute(attributeStr.trim(), null));
            }
        }
    }

    private List<String> getAttributeBody(String[] properties) {
        List<String> attributeBody = new ArrayList<>();
        boolean isBody = false;
        for (String property : properties) {
            property = property.trim();
            if (isBody) {
                if (property.equals("--")) {
                    break;
                }
                if (property.length() > 0) {
                    attributeBody.add(property);
                }
            } else {
                isBody = property.equals("--");
            }
        }
        return attributeBody;
    }
}
