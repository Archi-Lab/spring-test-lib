package thkoeln.st.springtestlib.relation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.GenericTests;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GenericBidirectionalTests extends GenericTests {

    public GenericBidirectionalTests(WebApplicationContext appContext) {
        super(appContext);
    }

    public void toManyNotBidirectionalTest(String parentClassPath, String childClassPath) throws Exception {
        Class parentClass = Class.forName(parentClassPath);
        Class childClass = Class.forName(childClassPath);

        for (Field declaredField : childClass.getDeclaredFields()) {
            if (Collection.class.isAssignableFrom(declaredField.getType())) {
                ParameterizedType listType = (ParameterizedType) declaredField.getGenericType();
                Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
                assertNotEquals(parentClass, listClass);
            }
        }
    }

    public void toOneNotBidirectionalTest(String parentClassPath, String childClassPath) throws Exception {
        Class parentClass = Class.forName(parentClassPath);
        Class childClass = Class.forName(childClassPath);

        for (Field declaredField : childClass.getDeclaredFields()) {
            assertNotEquals(parentClass, declaredField.getType());
        }
    }
}
