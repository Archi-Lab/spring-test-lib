package thkoeln.st.springtestlib.specification.diagram.parser.umlet.parser;

import thkoeln.st.springtestlib.specification.diagram.parser.ElementParser;
import thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements.UmletElement;

public abstract class UmletParser implements ElementParser<UmletElement> {

    protected String[] getSplittedProperties(UmletElement sourceElement) {
        return sourceElement.getPanelAttributes().split("\n");
    }
}
