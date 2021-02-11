package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser.UmlClassParser;

public enum UmletElementTypes {
    UML_CLASS("UMLClass", ElementType.CLASS, new UmlClassParser());

    private String elementName;
    private ElementType elementType;
    private ElementParser<UmletElement> elementParser;


    UmletElementTypes(String elementName, ElementType elementType, ElementParser<UmletElement> elementParser) {
        this.elementName = elementName;
        this.elementType = elementType;
        this.elementParser = elementParser;
    }

    public String getElementName() {
        return elementName;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public ElementParser<UmletElement> getElementParser() {
        return elementParser;
    }
}
