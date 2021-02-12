package thkoeln.st.springtestlib.specification.diagram.elements.implementations;

import thkoeln.st.springtestlib.specification.diagram.elements.*;

import java.util.List;

public class RelationElement extends LineElement {

    private static final float CONNECTION_ACCURACY = 5;

    private RectangularElement referencedElement1;
    private RectangularElement referencedElement2;


    public RelationElement() {
        super(ElementType.RELATION);
    }

    public RelationElement(Point start, Point end) {
        super(ElementType.RELATION, start, end);
    }

    @Override
    public void init(List<Element> elements) {
        referencedElement1 = getElementAtPos(elements, start);
        referencedElement2 = getElementAtPos(elements, end);
    }

    private RectangularElement getElementAtPos(List<Element> elements, Point pos) {
        for (Element element : elements) {
            if (element instanceof RectangularElement) {
                RectangularElement rectangularElement = (RectangularElement)element;
                if (checkOuterBorder(rectangularElement, pos) && !checkInnerBorder(rectangularElement, pos)) {
                    return rectangularElement;
                }
            }
        }
        return null;
    }

    private boolean checkOuterBorder(RectangularElement checkElement, Point pos) {
        return pos.getX() >= checkElement.getTopLeft().getX() - CONNECTION_ACCURACY
                && pos.getX() <= checkElement.getBottomRight().getX() + CONNECTION_ACCURACY
                && pos.getY() >= checkElement.getTopLeft().getY() - CONNECTION_ACCURACY
                && pos.getY() <= checkElement.getBottomRight().getY() + CONNECTION_ACCURACY;
    }

    private boolean checkInnerBorder(RectangularElement checkElement, Point pos) {
        return pos.getX() >= checkElement.getTopLeft().getX() + CONNECTION_ACCURACY
                && pos.getX() <= checkElement.getBottomRight().getX() - CONNECTION_ACCURACY
                && pos.getY() >= checkElement.getTopLeft().getY() + CONNECTION_ACCURACY
                && pos.getY() <= checkElement.getBottomRight().getY() - CONNECTION_ACCURACY;
    }

    public RectangularElement getReferencedElement1() {
        return referencedElement1;
    }

    public RectangularElement getReferencedElement2() {
        return referencedElement2;
    }
}
