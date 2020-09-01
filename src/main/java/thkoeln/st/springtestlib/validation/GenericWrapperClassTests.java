package thkoeln.st.springtestlib.validation;

import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;


public class GenericWrapperClassTests {

    /**
     * Checks if only wrapper classes are used as types for attributes
     * @param objectDescription description of the object which attributes should be checked
     * @throws Exception
     */
    public void onlyWrapperClassTest(ObjectDescription objectDescription) throws Exception {
        boolean containsPrimitives = false;

        Class objectClass = Class.forName(objectDescription.getClassPath());

        for (Attribute attribute : objectDescription.getAttributes()) {
            Field field = objectClass.getDeclaredField(attribute.getName());
            if (field.getType().isPrimitive()) {
                containsPrimitives = true;
            }
        }

        if (containsPrimitives) {
            throw new Exception("Do not use primitive Types");
        }
    }
}
