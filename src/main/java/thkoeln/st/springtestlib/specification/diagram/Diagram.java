package thkoeln.st.springtestlib.specification.diagram;

import thkoeln.st.springtestlib.specification.diagram.elements.Element;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Diagram {

    protected Map<ElementType, List<Element>> elementMap = new HashMap<>();
    protected List<Element> elements = new ArrayList<>();


    public void addElement(ElementType elementType, Element element) {
        elementMap.putIfAbsent(elementType, new ArrayList<>());
        elementMap.get(elementType).add(element);
        elements.add(element);
    }

    public void initElements() {
        for (Element element : elements) {
            element.init(elements);
        }
    }
}
