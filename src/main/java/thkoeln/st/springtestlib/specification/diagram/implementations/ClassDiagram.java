package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.relationelement.RelationElement;

import java.util.InputMismatchException;
import java.util.List;

public class ClassDiagram extends Diagram {

    @Override
    public void compareToActualDiagram(Diagram actualDiagram) {
        assertClasses(actualDiagram);
        assertRelations(actualDiagram);
    }

    // Assert Classes
    private void assertClasses(Diagram actualDiagram) {
        List<ClassElement> expectedClassElements = getElementsByType(ElementType.CLASS);
        List<ClassElement> actualClassElements = actualDiagram.getElementsByType(ElementType.CLASS);

        for (ClassElement expectedClassElement : expectedClassElements) {
            compareExpectedClassWithActualClasses(expectedClassElement, actualClassElements);
        }

        if (expectedClassElements.size() > actualClassElements.size()) {
            throw new InputMismatchException("Too many classes");
        }
    }

    private void compareExpectedClassWithActualClasses(ClassElement expectedClassElement, List<ClassElement> actualClassElements) {
        boolean found = false;
        for (ClassElement actualClassElement : actualClassElements) {
            if (expectedClassElement.equals(actualClassElement)) {
                if (found) {
                    throw new InputMismatchException("There are multiple classes with name: " + expectedClassElement.getId());
                }

                compareExpectedClassWithActualClass(expectedClassElement, actualClassElement);
                found = true;
            }
        }

        if (!found) {
            throw new InputMismatchException("Class not found with id: " + expectedClassElement.getId());
        }
    }

    private void compareExpectedClassWithActualClass(ClassElement expectedClassElement, ClassElement actualClassElement) {
        // TODO
    }

    // Assert Relations
    private void assertRelations(Diagram actualDiagram) {
        List<RelationElement> expectedRelationElements = getElementsByType(ElementType.RELATION);
        List<RelationElement> actualRelationElements = actualDiagram.getElementsByType(ElementType.RELATION);

        for (RelationElement expectedRelationElement : expectedRelationElements) {
            compareExpectedRelationWithActualRelations(expectedRelationElement, actualRelationElements);
        }

        if (expectedRelationElements.size() > actualRelationElements.size()) {
            throw new InputMismatchException("Too many Relations");
        }
    }

    private void compareExpectedRelationWithActualRelations(RelationElement expectedRelationElement, List<RelationElement> actualRelationElements) {
        boolean found = false;
        for (RelationElement actualRelationElement : actualRelationElements) {
            if (expectedRelationElement.compareToRelationAndSwitchDirectionIfNeccessary(actualRelationElement)) {
                if (found) {
                    throw new InputMismatchException("There are multiple relations connecting classes: "
                        + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
                }

                compareExpectedRelationWithActualRelation(expectedRelationElement, actualRelationElement);
                found = true;
            }
        }

        if (!found) {
            throw new InputMismatchException("A certain relationelement was not found");
        }
    }

    private void compareExpectedRelationWithActualRelation(RelationElement expectedRelationElement, RelationElement actualRelationElement) {
        if (expectedRelationElement.getRelationLineType() != actualRelationElement.getRelationLineType()) {
            throw new InputMismatchException("Relation line type is not valid for relation between classes: "
                + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (expectedRelationElement.getRelationPointer1().getRelationArrowType() != actualRelationElement.getRelationPointer1().getRelationArrowType()
            || expectedRelationElement.getRelationPointer2().getRelationArrowType() != actualRelationElement.getRelationPointer2().getRelationArrowType()) {
            throw new InputMismatchException("At least one relation arrow type is not valid for relation between classes: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }

        if (!expectedRelationElement.getRelationPointer1().getCardinality().equals(actualRelationElement.getRelationPointer1().getCardinality())
                || !expectedRelationElement.getRelationPointer2().getCardinality().equals(actualRelationElement.getRelationPointer2().getCardinality())) {
            throw new InputMismatchException("At least one relation cardinality is not valid for relation between classes: "
                    + expectedRelationElement.getReferencedElement1().getId() + ", " + expectedRelationElement.getReferencedElement2().getId());
        }
    }
}
