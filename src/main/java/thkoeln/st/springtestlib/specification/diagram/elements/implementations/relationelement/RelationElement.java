package thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement;

import thkoeln.st.springtestlib.specification.diagram.elements.*;

import java.util.InputMismatchException;
import java.util.List;

public class RelationElement extends LineElement {

    private static final float CONNECTION_ACCURACY = 5;

    private RelationPointer relationPointer1 = new RelationPointer();
    private RelationPointer relationPointer2 = new RelationPointer();

    private RelationLineType relationLineType;


    public RelationElement() {
        super(ElementType.RELATION);
    }

    public RelationElement(Point start, Point end) {
        super(ElementType.RELATION, start, end);
    }

    @Override
    public void init(List<Element> elements) {
        relationPointer1.setConnectedElement(getElementAtPos(elements, start));
        relationPointer2.setConnectedElement(getElementAtPos(elements, end));

        if (relationPointer1.getConnectedElement() == null || relationPointer2.getConnectedElement() == null) {
            throw new InputMismatchException("Could not connect relationelement");
        }
    }

    private void switchDirection() {
        RelationPointer tmp = relationPointer1;
        relationPointer1 = relationPointer2;
        relationPointer2 = tmp;
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

    public boolean compareToRelationAndSwitchDirectionIfNeccessary(RelationElement relationElement) {
        if (getReferencedElement1().equals(relationElement.getReferencedElement1())
            && getReferencedElement2().equals(relationElement.getReferencedElement2())) {
            return true;
        }
        if (getReferencedElement1().equals(relationElement.getReferencedElement2())
            && getReferencedElement2().equals(relationElement.getReferencedElement1())) {
            relationElement.switchDirection();
            return true;
        }
        return false;
    }

    public RelationPointer getRelationPointer1() {
        return relationPointer1;
    }

    public RelationPointer getRelationPointer2() {
        return relationPointer2;
    }

    public RectangularElement getReferencedElement1() {
        return relationPointer1.getConnectedElement();
    }

    public RectangularElement getReferencedElement2() {
        return relationPointer2.getConnectedElement();
    }

    public RelationLineType getRelationLineType() {
        return relationLineType;
    }

    public void setRelationLineType(RelationLineType relationLineType) {
        this.relationLineType = relationLineType;
    }
}
