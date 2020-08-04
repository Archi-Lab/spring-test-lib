package thkoeln.st.springtestlib.validation;

import java.lang.reflect.Field;

public class GenericWrapperClassTests {

    public void onlyWrapperClassTest(String className, String[] fieldNames) throws Exception {
        boolean containsPrimitives = false;

        Class objectClass = Class.forName(className);

        for (int i = 0; i < fieldNames.length; i++) {
            Field field = objectClass.getDeclaredField(fieldNames[i]);
            if (field.getType().isPrimitive()) {
                containsPrimitives = true;
            }
        }

        if (containsPrimitives) {
            throw new Exception("Do not use primitive Types");
        }
    }
}
