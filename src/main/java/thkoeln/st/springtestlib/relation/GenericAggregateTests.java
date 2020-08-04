package thkoeln.st.springtestlib.relation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class GenericAggregateTests extends GenericTests {

    private static final int COLLECTION_OBJECT_COUNT = 2;

    public GenericAggregateTests(WebApplicationContext appContext) {
        super(appContext);
    }

    public void noRepositoryForClassTest(String referencedClassClassPath) throws Exception {
        boolean noRepoForReferencedEntity = false;

        try {
            oir.getRepository(referencedClassClassPath);
        } catch (NoSuchElementException e) {
            noRepoForReferencedEntity = true;
        }

        assertTrue(noRepoForReferencedEntity);
    }

    public void referencedObjectAsCopyOrNoSetterTest(String parentClassPath, Attribute[] parentAttributes, String childClassPath, Attribute[] childAttributes, String childAttributeName, String childGetterName) throws Exception {
        assertTrue(
            referencedObjectAsCopyTest(parentClassPath, parentAttributes, childClassPath, childAttributes, childAttributeName, childGetterName) ||
                    noSetterInVOTest(childClassPath, childAttributes)
        );
    }

    public void referencedObjectCollectionAsCopyOrNoSetterTest(String parentClassPath, Attribute[] parentAttributes, String childClassPath, Attribute[] childAttributes, String childAttributeName, String childGetterName) throws Exception {
        assertTrue(
            referencedObjectCollectionAsCopyTest(parentClassPath, parentAttributes, childClassPath, childAttributes, childAttributeName, childGetterName) ||
                    noSetterInVOTest(childClassPath, childAttributes)
        );
    }

    private boolean referencedObjectAsCopyTest(String parentClassPath, Attribute[] parentAttributes, String childClassPath, Attribute[] childAttributes, String childAttributeName, String childGetterName) throws Exception {
        // Create Child
        Object childObject = objectBuilder.buildObject(childClassPath, childAttributes);

        // Create Parent
        Object parentObject = objectBuilder.buildObject(parentClassPath, parentAttributes);
        Field childField = parentObject.getClass().getDeclaredField(childAttributeName);
        childField.setAccessible(true);
        childField.set(parentObject, childObject);

        // Retrieve Child
        Method getRelationMethod = parentObject.getClass().getMethod(childGetterName);
        Object retrievedChildObject = getRelationMethod.invoke(parentObject);

        return childObject != retrievedChildObject;
    }

    private boolean referencedObjectCollectionAsCopyTest(String parentClassPath, Attribute[] parentAttributes, String childClassPath, Attribute[] childAttributes, String childAttributeName, String childGetterName) throws Exception {
        // Create Childs
        List<Object> childObjects = objectBuilder.buildObjectList(childClassPath, childAttributes, COLLECTION_OBJECT_COUNT);

        // Create Parent
        Object parentObject = objectBuilder.buildObject(parentClassPath, parentAttributes);
        Field childField = parentObject.getClass().getDeclaredField(childAttributeName);
        childField.setAccessible(true);
        childField.set(parentObject, childObjects);

        // Retrieve Childs
        Method getRelationMethod = parentObject.getClass().getMethod(childGetterName);
        List<Object> retrievedChildObjects = (List)getRelationMethod.invoke(parentObject);

        // Assert not Same
        for (Object unexpectedChildObject : childObjects) {
            for (Object retrievedChildObject : retrievedChildObjects) {
                if (unexpectedChildObject == retrievedChildObject) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean noSetterInVOTest(String valueObjectClassPath, Attribute[] valueObjectAttributes) throws Exception {
        Class valueObjectClass = Class.forName(valueObjectClassPath);

        for (Method declaredMethod : valueObjectClass.getDeclaredMethods()) {
            if (declaredMethod.getGenericParameterTypes().length == 1) {
                Type genericParameterType = declaredMethod.getGenericParameterTypes()[0];
                for (Attribute valueObjectAttribute : valueObjectAttributes) {
                    if (genericParameterType.getTypeName().contains(valueObjectAttribute.getType())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
