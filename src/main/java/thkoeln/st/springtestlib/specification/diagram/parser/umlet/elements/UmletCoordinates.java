package thkoeln.st.springtestlib.specification.diagram.parser.umlet.elements;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("coordinates")
public class UmletCoordinates {

    @XStreamAlias("x")
    private int x;

    @XStreamAlias("y")
    private int y;

    @XStreamAlias("w")
    private int width;

    @XStreamAlias("h")
    private int height;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
