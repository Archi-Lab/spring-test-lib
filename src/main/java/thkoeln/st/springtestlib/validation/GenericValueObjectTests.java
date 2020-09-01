package thkoeln.st.springtestlib.validation;

import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.annotation.Annotation;


public class GenericValueObjectTests {

    /**
     * Checks if the given object is implemented as a value object
     * @param valueObjectDescription description of the right object
     * @throws Exception
     */
    public void correctValueObjectTest(ObjectDescription valueObjectDescription) throws Exception {
        try {
            // Retrieve Classes
            Class valueObjectClass = Class.forName(valueObjectDescription.getClassPath());

            // Test Embeddable on Value Object
            Annotation[] valueObjectAnnotations = valueObjectClass.getAnnotations();
            boolean containsEmbeddable = false;
            for (Annotation annotation : valueObjectAnnotations) {
                if (annotation.toString().equals("@javax.persistence.Embeddable()")) {
                    containsEmbeddable = true;
                }
            }

            if (!containsEmbeddable) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("You have the wrong Value Object");
        }
    }
}
