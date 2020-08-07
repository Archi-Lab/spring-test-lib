package thkoeln.st.springtestlib.relation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GenericRelationsTests extends GenericTests {

    private static final int LIST_COUNT = 3;

    public GenericRelationsTests(WebApplicationContext appContext) {
        super(appContext);
    }

    // Generic Test Methods
    public void oneToOneTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childSetterName, String childGetterName) throws Exception {
        // Create Parent & Child object
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        // Retrieve Methods
        Method setRelationMethod = parentObject.getClass().getMethod(childSetterName, childObject.getClass());
        Method getRelationMethod = parentObject.getClass().getMethod(childGetterName);

        // Save parent with child as attribute and child to repository
        setRelationMethod.invoke(parentObject, childObject);
        oir.getRepository(childObjectDescription.getClassPath()).save(childObject);
        oir.getRepository(parentObjectDescription.getClassPath()).save(parentObject);

        // Retrieve parent with child as attribute from repository
        Object retrievedParentObject = oir.getRepository(parentObjectDescription.getClassPath()).findAll().iterator().next();
        Object retrievedChildObject = getRelationMethod.invoke(retrievedParentObject);
        assertEquals(childObject, retrievedChildObject);
    }

    public void oneToOneVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName) throws Exception {
        // Create Parent & Child object
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        // Retrieve Field
        Field field = parentObject.getClass().getDeclaredField(childAttributeName);

        // Save parent with child as attribute and child to repository
        field.setAccessible(true);
        field.set(parentObject, childObject);
        oir.getRepository(parentObjectDescription.getClassPath()).save(parentObject);

        // Retrieve parent with child as attribute from repository
        Object retrievedParentObject = oir.getRepository(parentObjectDescription.getClassPath()).findAll().iterator().next();
        Object retrievedChildObject = field.get(retrievedParentObject);
        assertEquals(childObject, retrievedChildObject);
    }

    public void oneToManyVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName) throws Exception {
        // Create Parent & Child object
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, LIST_COUNT);

        // Retrieve Field
        Field field = parentObject.getClass().getDeclaredField(childAttributeName);

        // Save parent with child as attribute and child to repository
        field.setAccessible(true);
        field.set(parentObject, childObjects);
        oir.getRepository(parentObjectDescription.getClassPath()).save(parentObject);

        // Retrieve parent with child as attribute from repository
        Object retrievedParentObject = oir.getRepository(parentObjectDescription.getClassPath()).findAll().iterator().next();
        List<Object> retrievedChildObjects = (List<Object>)field.get(retrievedParentObject);
        assertEquals(childObjects.size(), retrievedChildObjects.size());
        for (int i = 0; i < childObjects.size(); i++) {
            assertEquals(childObjects.get(i), retrievedChildObjects.get(i));
        }
    }

    public void manyToOneTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childSetterName, String childGetterName) throws Exception {
        oneToOneTest(parentObjectDescription, childObjectDescription, childSetterName, childGetterName);
    }

    public void oneToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childSetterName, String childGetterName) throws Exception {
        // Retrieve Classes and Methods
        Class parentClass = Class.forName(parentObjectDescription.getClassPath());
        Method setRelationMethod = parentClass.getMethod(childSetterName, List.class);
        Method getRelationMethod = parentClass.getMethod(childGetterName);

        // Create Objects
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, LIST_COUNT);

        // Save parent object with multiple child objects as attribute and child object to repository
        setRelationMethod.invoke(parentObject, childObjects);
        for (Object childObject : childObjects) {
            oir.getRepository(childObjectDescription.getClassPath()).save(childObject);
        }
        oir.getRepository(parentObjectDescription.getClassPath()).save(parentObject);

        // Retrieve parent object with multiple child objects as attribute from repository
        Object retrievedParentObject = oir.getRepository(parentObjectDescription.getClassPath()).findAll().iterator().next();
        List<Object> retrievedChildObjects = (List<Object>) getRelationMethod.invoke(retrievedParentObject);

        assertEquals(LIST_COUNT, retrievedChildObjects.size());
        for (int i = 0; i < childObjects.size(); i++) {
            assertEquals(childObjects.get(i), retrievedChildObjects.get(i));
        }
    }

    public void manyToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childSetterName, String childGetterName, String childFinderName) throws Exception {
        // Retrieve Classes and Methods
        Class parentClass = Class.forName(parentObjectDescription.getClassPath());
        Method setRelationMethod = parentClass.getMethod(childSetterName, List.class);
        Method getRelationMethod = parentClass.getMethod(childGetterName);

        // Create Objects
        List<Object> parentObjects = objectBuilder.buildObjectList(parentObjectDescription, LIST_COUNT);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, LIST_COUNT);

        // Save multiple parent objects with multiple child objects as attribute and child objects to repository
        for (Object parentObject : parentObjects) {
            setRelationMethod.invoke(parentObject, childObjects);
        }

        for (Object childObject : childObjects) {
            oir.getRepository(childObjectDescription.getClassPath()).save(childObject);
        }

        for (Object parentObject : parentObjects) {
            oir.getRepository(parentObjectDescription.getClassPath()).save(parentObject);
        }

        // Retrieve parent object with multiple child Objects as attribute from repository
        for (Object retrievedParentObject : oir.getRepository(parentObjectDescription.getClassPath()).findAll()) {
            List<Object> retrievedChildObjects = (List<Object>) getRelationMethod.invoke(retrievedParentObject);

            assertEquals(LIST_COUNT, retrievedChildObjects.size());
            for (int i = 0; i < childObjects.size(); i++) {
                assertEquals(childObjects.get(i), retrievedChildObjects.get(i));
            }
        }

        finderMethodTest(parentObjectDescription.getClassPath(), parentObjects, childObjectDescription.getClassPath(), childObjects, childFinderName);
    }

    private void finderMethodTest(String parentClassPath, List<Object> parentObjects, String childClassPath, List<Object> childObjects, String childFinderName) throws Exception {
        Class childClass = Class.forName(childClassPath);
        Method finderMethod = oir.getRepository(parentClassPath).getClass().getMethod(childFinderName, childClass);

        for (Object childObject : childObjects) {
            List<Object> retrievedParentObjects = (List<Object>) finderMethod.invoke(oir.getRepository(parentClassPath), childObject);

            assertEquals(new HashSet<>(parentObjects), new HashSet<>(retrievedParentObjects));
        }
    }
}
