package thkoeln.st.springtestlib.specification.diagram.elements;

import java.util.List;
import java.util.UUID;

public abstract class Element {

    private String id;
    private ElementType elementType;


    public Element(ElementType elementType) {
        this.elementType = elementType;

        id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Element)) {
            return false;
        }

        return (id.equalsIgnoreCase(((Element)obj).getId()));
    }

    public void init(List<Element> elements){ }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ElementType getElementType() {
        return elementType;
    }
}
