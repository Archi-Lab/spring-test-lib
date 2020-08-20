package thkoeln.st.springtestlib.relation;

import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

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

    public void noRepositoryForClassTest(ObjectDescription objectDescription) throws Exception {
        boolean noRepoForReferencedEntity = false;

        try {
            oir.getRepository(objectDescription.getClassPath());
        } catch (NoSuchElementException e) {
            noRepoForReferencedEntity = true;
        }

        assertTrue(noRepoForReferencedEntity);
    }

    public void referencedObjectAsCopyOrNoSetterTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        assertTrue(
            referencedObjectAsCopyTest(parentObjectDescription, childObjectDescription) ||
                    noSetterInVOTest(childObjectDescription.getClassPath(), childObjectDescription.getAttributes())
        );
    }

    public void referencedObjectCollectionAsCopyOrNoSetterTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        assertTrue(
            referencedObjectCollectionAsCopyTest(parentObjectDescription, childObjectDescription) ||
                    noSetterInVOTest(childObjectDescription.getClassPath(), childObjectDescription.getAttributes())
        );
    }

    private boolean referencedObjectAsCopyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Create Child
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        // Create Parent
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributeSingular());
        childField.setAccessible(true);
        childField.set(parentObject, childObject);

        // Retrieve Child
        Method getRelationMethod = parentObject.getClass().getMethod(childObjectDescription.getGetToOne());
        Object retrievedChildObject = getRelationMethod.invoke(parentObject);

        return childObject != retrievedChildObject;
    }

    private boolean referencedObjectCollectionAsCopyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Create Childs
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_OBJECT_COUNT);

        // Create Parent
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributePlural());
        childField.setAccessible(true);
        childField.set(parentObject, childObjects);

        // Retrieve Childs
        Method getRelationMethod = parentObject.getClass().getMethod(childObjectDescription.getGetToMany());
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
