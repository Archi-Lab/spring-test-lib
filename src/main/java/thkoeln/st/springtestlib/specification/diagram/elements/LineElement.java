package thkoeln.st.springtestlib.specification.diagram.elements;

public abstract class LineElement extends Element {

    protected Point start;
    protected Point end;


    public LineElement(ElementType elementType) {
        super(elementType);
    }

    public LineElement(ElementType elementType, Point start, Point end) {
        super(elementType);
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
