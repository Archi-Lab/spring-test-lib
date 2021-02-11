package thkoeln.st.springtestlib.specification.diagram.elements;


public abstract class RectangularElement extends Element {

    private Integer xMin;
    private Integer yMin;
    private Integer xMax;
    private Integer yMax;
    private Integer width;
    private Integer height;


    public RectangularElement(ElementType elementType) {
        super(elementType);
    }

    public RectangularElement(ElementType elementType, Integer xMin, Integer yMin, Integer width, Integer height) {
        super(elementType);
        this.xMin = xMin;
        this.yMin = yMin;
        this.width = width;
        this.height = height;
        this.xMax = xMin + width;
        this.yMax = yMin + height;
    }

    public Integer getxMin() {
        return xMin;
    }

    public Integer getyMin() {
        return yMin;
    }

    public Integer getxMax() {
        return xMax;
    }

    public Integer getyMax() {
        return yMax;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
