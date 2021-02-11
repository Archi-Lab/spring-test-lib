package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import thkoeln.st.springtestlib.specification.diagram.parser.SourceElement;

@XStreamAlias("element")
public class UmletElement implements SourceElement {

    @XStreamAlias("id")
    private String id;

    @XStreamAlias("panel_attributes")
    private String panelAttributes;

    @XStreamAlias("additional_attributes")
    private String additionalAttributes;

    @XStreamAlias("coordinates")
    private UmletCoordinates umletCoordinates;


    public String getId() {
        return id;
    }

    public String getPanelAttributes() {
        return panelAttributes;
    }

    public String getAdditionalAttributes() {
        return additionalAttributes;
    }

    public UmletCoordinates getUmletCoordinates() {
        return umletCoordinates;
    }
}
