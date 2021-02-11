package thkoeln.st.springtestlib.specification.diagram.elements;

import java.util.UUID;

public abstract class Element {

    private String id;
    private ElementType elementType;


    public Element(ElementType elementType) {
        this.elementType = elementType;

        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public ElementType getElementType() {
        return elementType;
    }
}
