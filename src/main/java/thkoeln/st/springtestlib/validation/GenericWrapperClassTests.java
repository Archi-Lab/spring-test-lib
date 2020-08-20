package thkoeln.st.springtestlib.validation;

import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;

public class GenericWrapperClassTests {

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
