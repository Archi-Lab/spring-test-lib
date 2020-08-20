package thkoeln.st.springtestlib.relation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GenericBidirectionalTests extends GenericTests {

    public GenericBidirectionalTests(WebApplicationContext appContext) {
        super(appContext);
    }

    public void toManyNotBidirectionalTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        Class parentClass = Class.forName(parentObjectDescription.getClassPath());
        Class childClass = Class.forName(childObjectDescription.getClassPath());

        for (Field declaredField : childClass.getDeclaredFields()) {
            if (Collection.class.isAssignableFrom(declaredField.getType())) {
                ParameterizedType listType = (ParameterizedType) declaredField.getGenericType();
                Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                assertNotEquals(parentClass, listClass);
            }
        }
    }

    public void toOneNotBidirectionalTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        Class parentClass = Class.forName(parentObjectDescription.getClassPath());
        Class childClass = Class.forName(childObjectDescription.getClassPath());

        for (Field declaredField : childClass.getDeclaredFields()) {
            assertNotEquals(parentClass, declaredField.getType());
        }
    }
}
