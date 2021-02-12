package thkoeln.st.springtestlib.specification.diagram.elements;


public abstract class RectangularElement extends Element {

    protected Point topLeft;
    protected Point bottomRight;
    protected float width;
    protected float height;


    public RectangularElement(ElementType elementType) {
        super(elementType);
    }

    public RectangularElement(ElementType elementType, Point topLeft, float width, float height) {
        super(elementType);
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
        this.bottomRight = new Point(topLeft.getX() + width, topLeft.getY() + height);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }
}
