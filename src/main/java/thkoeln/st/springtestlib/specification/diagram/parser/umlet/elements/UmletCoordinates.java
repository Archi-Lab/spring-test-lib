package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("coordinates")
public class UmletCoordinates {

    @XStreamAlias("x")
    private Integer x;

    @XStreamAlias("y")
    private Integer y;

    @XStreamAlias("w")
    private Integer width;

    @XStreamAlias("h")
    private Integer height;


    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
