package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("diagram")
public class UmletDiagram {

    @XStreamImplicit(itemFieldName = "element")
    private List<UmletElement> umletElements;


    public List<UmletElement> getUmletElements() {
        return umletElements;
    }
}
