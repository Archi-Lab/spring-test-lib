package thkoeln.st.springtestlib.specification.diagram;

import thkoeln.st.springtestlib.specification.diagram.elements.Element;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Diagram {

    protected Map<ElementType, List<Element>> elements = new HashMap<>();


    public void addElement(ElementType elementType, Element element) {
        elements.putIfAbsent(elementType, new ArrayList<>());
        elements.get(elementType).add(element);
    }


}
