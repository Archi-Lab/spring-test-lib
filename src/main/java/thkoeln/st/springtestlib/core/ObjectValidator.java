package thkoeln.st.springtestlib.core;

import com.jayway.jsonpath.JsonPath;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Contains numerous ways of validating objects and result actions
 */
public class ObjectValidator {

    private ObjectBuilder objectBuilder;
    private ObjectInfoRetriever objectInfoRetriever;


    public ObjectValidator(ObjectBuilder objectBuilder, ObjectInfoRetriever objectInfoRetriever) {
        this.objectBuilder = objectBuilder;
        this.objectInfoRetriever = objectInfoRetriever;
    }

    /**
     * Validates an object based on given attributes
     * @param expectedObject expected object
     * @param validateObject actual object
     * @param attributes base of validation
     * @throws Exception
     */
    public void validateTwoObjects(Object expectedObject, Object validateObject, Attribute[] attributes) throws Exception {
        for (Field field : expectedObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (objectBuilder.isValueObject(field)) {
                Object expectedValueObject = field.get(expectedObject);
                Object validateValueObject = field.get(validateObject);
                for (Field valueObjectField : expectedValueObject.getClass().getDeclaredFields()) {
                    valueObjectField.setAccessible(true);
                    if (doesAttributeExist(attributes, valueObjectField.getName())) {
                        assertEquals(valueObjectField.get(expectedValueObject), valueObjectField.get(validateValueObject));
                    }
                }
            } else {
                if (doesAttributeExist(attributes, field.getName())) {
                    assertEquals(field.get(expectedObject), field.get(validateObject));
                }
            }
        }
    }

    /**
     * Checks if all attributes of an object are contained in the resultActions
     * @param object expected object inside the result actions
     * @param resultActions actual resultActions
     * @param attributes visible attributes
     * @param preIdentifier used to validate result actions of objects inside lists e.g.: .[0] = first element in list
     * @throws Exception
     */
    public void validateResultActions(Object object, ResultActions resultActions, Attribute[] attributes, String preIdentifier) throws Exception {
        validateResultActions(object, resultActions, attributes, new Attribute[]{}, preIdentifier);
    }

    /**
     * Checks if all attributes of an object are contained in the resultActions
     * @param object expected object inside the result actions
     * @param resultActions actual resultActions
     * @param attributes visible attributes
     * @param hiddenAttributes hidden attributes
     * @param preIdentifier used to validate result actions of objects inside lists e.g.: .[0] = first element in list
     * @throws Exception
     */
    public void validateResultActions(Object object, ResultActions resultActions, Attribute[] attributes, Attribute[] hiddenAttributes, String preIdentifier) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (objectBuilder.isValueObject(field)) {
                Object valueObject = field.get(object);
                if (valueObject != null) {
                    for (Field valueObjectField : valueObject.getClass().getDeclaredFields()) {
                        valueObjectField.setAccessible(true);
                        assertResultActionsAttribute(resultActions, valueObject, field, valueObjectField, attributes, hiddenAttributes, preIdentifier);
                    }
                }
            } else {
                assertResultActionsAttribute(resultActions, object, null, field, attributes, hiddenAttributes, preIdentifier);
            }
        }
    }

    /**
     * Checks if all relevant links are contained in the resultActions
     * @param linkRelevantObjects objects which are used to replace identifiers inside abstract links
     * @param resultActions actual resultActions
     * @param expectedLinks expected links
     * @param hiddenLinks hidden links
     * @param preIdentifier used to validate result action links of objects inside lists e.g.: .[0] = first element in list
     * @throws Exception
     */
    public void validateResultActionLinks(List<Object> linkRelevantObjects, ResultActions resultActions, Link[] expectedLinks, Link[] hiddenLinks, String preIdentifier) throws Exception {
        List<UUID> linkRelevantIds = new ArrayList<>();
        for (Object linkRelevantObject : linkRelevantObjects) {
            linkRelevantIds.add(objectInfoRetriever.getId(linkRelevantObject));
        }

        for (Link hiddenLink : hiddenLinks) {
            String linkPath = "$" + preIdentifier + "._links." + hiddenLink.getRelation() + ".href";
            resultActions.andExpect(jsonPath(linkPath).doesNotExist());
        }

        for (Link expectedLink : expectedLinks) {
            String linkPath = "$" + preIdentifier + "._links." + expectedLink.getRelation() + ".href";
            String testLinkValue = JsonPath.read(resultActions.andReturn().getResponse().getContentAsString(), linkPath);
            testLinkValue = Link.getDomainLessLink(testLinkValue);

            expectedLink.calculateCustomizedLink(linkRelevantIds);
            String customizedLink = expectedLink.getCustomizedLink();

            assertTrue(expectedLink.equals(testLinkValue),
                    "Expected Link: " + customizedLink
                            + "  Actual Link: " + testLinkValue);
        }
    }

    private void assertResultActionsAttribute(ResultActions resultActions,
                                              Object object,
                                              Field wrapperField,
                                              Field field,
                                              Attribute[] attributes,
                                              Attribute[] hiddenAttributes,
                                              String preIdentifier) throws Exception {
        String fieldPath = "$" + preIdentifier;
        if (wrapperField != null) {
            fieldPath += "." + wrapperField.getName();
        }
        fieldPath += "." + field.getName();

        if (doesAttributeExist(hiddenAttributes, field.getName())) {
            assertFalse(doesFieldContainAnnotationName(field, "JsonIgnore"));
            resultActions.andExpect(jsonPath(fieldPath).doesNotExist());
        } else if (doesAttributeExist(attributes, field.getName())) {
            resultActions.andExpect(jsonPath(fieldPath).value(field.get(object)));
        }
    }

    /**
     * Checks if a field contains a certain annotation
     * @param field the field to be tested
     * @param annotationName the annotation to be tested
     * @return true if the field contains the given annotation
     */
    public boolean doesFieldContainAnnotationName(Field field, String annotationName) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.toString().contains(annotationName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean doesAttributeExist(Attribute[] attributes, String name) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Asserts a relation which has one child
     * @param parentRepository repository of the parent object
     * @param parentObject parent of the relation ship
     * @param expectedChildObject expected child of the relation ship
     * @param childGetterName name of the getChild method
     * @param compareIds if true ids are compared, else the equals method is used
     * @throws Exception
     */
    public void assertToOneRelation(CrudRepository parentRepository, Object parentObject, Object expectedChildObject, String childGetterName, boolean compareIds) throws Exception {
        Optional<Object> retrievedParentObjectOpt = parentRepository.findById(objectInfoRetriever.getId(parentObject));
        assertTrue(retrievedParentObjectOpt.isPresent());
        Object retrievedParentObject = retrievedParentObjectOpt.get();

        Method getRelationMethod = retrievedParentObject.getClass().getMethod(childGetterName);
        Object retrievedChildObject = getRelationMethod.invoke(retrievedParentObject);

        if (expectedChildObject == null) {
            assertNull(retrievedChildObject);
        } else {
            compareTwoObjects(expectedChildObject, retrievedChildObject, compareIds);
        }
    }

    /**
     * Asserts a relation which has multiple children
     * @param parentRepository repository of the parent object
     * @param parentObject parent of the relation ship
     * @param expectedChildObjects expected list of children of the relation ship
     * @param childGetterName name of the getChildren method
     * @param compareIds if true ids are compared, else the equals method is used
     * @throws Exception
     */
    public void assertToManyRelation(CrudRepository parentRepository, Object parentObject, List<Object> expectedChildObjects, String childGetterName, boolean compareIds) throws Exception {
        Optional<Object> retrievedParentObjectOpt = parentRepository.findById(objectInfoRetriever.getId(parentObject));
        assertTrue(retrievedParentObjectOpt.isPresent());
        Object retrievedParentObject = retrievedParentObjectOpt.get();

        Method getRelationMethod = retrievedParentObject.getClass().getMethod(childGetterName);
        List<Object> retrievedChildObjects = (List<Object>) getRelationMethod.invoke(retrievedParentObject);

        assertEquals(expectedChildObjects.size(), retrievedChildObjects.size());

        for (int i = 0; i < expectedChildObjects.size(); i++) {
            compareTwoObjects(expectedChildObjects.get(i), retrievedChildObjects.get(i), compareIds);
        }
    }

    private void compareTwoObjects(Object expectedObject, Object testObject, boolean compareIds) throws Exception {
        if (compareIds) {
            assertEquals(objectInfoRetriever.getId(expectedObject),
                    objectInfoRetriever.getId(testObject),
                    "Hint: Have you implemented an equals() method? If not, this test will always fail.");
        } else {
            assertEquals(expectedObject, testObject,
                    "Hint: Have you implemented an equals() method? If not, this test will always fail.");
        }
    }
}
