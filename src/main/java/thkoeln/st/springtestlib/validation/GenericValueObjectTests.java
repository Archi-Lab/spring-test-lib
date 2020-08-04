package thkoeln.st.springtestlib.validation;

import java.lang.annotation.Annotation;

public class GenericValueObjectTests {

    public void correctValueObjectTest(String valueObjectClassName) throws Exception {
        try {
            // Retrieve Classes
            Class valueObjectClass = Class.forName(valueObjectClassName);

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
