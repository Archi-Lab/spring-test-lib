package thkoeln.st.springtestlib.specification.diagram.implementations;

import thkoeln.st.springtestlib.specification.diagram.Diagram;
import thkoeln.st.springtestlib.specification.diagram.elements.ElementType;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.ClassElement;
import thkoeln.st.springtestlib.specification.diagram.elements.implementations.RelationElement;

import java.util.InputMismatchException;
import java.util.List;

public class ClassDiagram extends Diagram {

    @Override
    public void compareToActualDiagram(Diagram actualDiagram) {
        assertClasses(actualDiagram);
        assertRelations(actualDiagram);
    }

    private void assertClasses(Diagram actualDiagram) {
        List<ClassElement> expectedClassElements = getElementsByType(ElementType.CLASS);
        List<ClassElement> actualClassElements = actualDiagram.getElementsByType(ElementType.CLASS);

        for (ClassElement expectedClassElement : expectedClassElements) {
            assertClass(expectedClassElement, actualClassElements);
        }
    }

    private void assertClass(ClassElement expectedClassElement, List<ClassElement> actualClassElements) {
        boolean found = false;
        for (ClassElement actualClassElement : actualClassElements) {
            if (expectedClassElement.equals(actualClassElement)) {
                if (found) {
                    throw new InputMismatchException("There are multiple classes with name: " + expectedClassElement.getId());
                }

                // TODO compare class elements
                found = true;
            }
        }

        if (!found) {
            throw new InputMismatchException("Class not found with id: " + expectedClassElement.getId());
        }
    }

    private void assertRelations(Diagram actualDiagram) {

    }

    private void assertRelation(RelationElement expectedRelationElement, RelationElement actualRelationElement) {

    }
}
