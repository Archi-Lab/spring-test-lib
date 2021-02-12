package thkoeln.st.springtestlib.specification.diagram.elements;

public class Point {

    private float x;
    private float y;


    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Point p) {
        this.x += p.getX();
        this.y += p.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
