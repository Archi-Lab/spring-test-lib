package thkoeln.st.springtestlib.controller.level2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.ObjectDescription;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenericControllerAssociationTests extends GenericTests {

    private static final int COLLECTION_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerAssociationTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public void putOneToOneTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childGetterName) throws Exception {
        // Save Parent
        CrudRepository parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        parentRepository.save(parentObject);

        // Save Child
        CrudRepository childRepository = oir.getRepository(childObjectDescription.getClassPath());
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        childRepository.save(childObject);

        // Perform put
        mockMvc
                .perform(put(parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        objectValidator.assertToOneRelation(parentRepository, parentObject, childObject, childGetterName, true);
    }

    public void getOneToOneTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childSetterName) throws Exception {
        // Save Child
        CrudRepository childRepository = oir.getRepository(childObjectDescription.getClassPath());
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        childRepository.save(childObject);

        // Save Parent
        CrudRepository parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Method setRelationMethod = parentObject.getClass().getMethod(childSetterName, childObject.getClass());
        setRelationMethod.invoke(parentObject, childObject);
        parentRepository.save(parentObject);

        // Perform get
        mockMvc
                .perform(get(parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childAttributeName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(oir.getId(childObject).toString()));
    }

    public void deleteOneToOneTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childSetterName, String childGetterName) throws Exception {
        // Save Child
        CrudRepository childRepository = oir.getRepository(childObjectDescription.getClassPath());
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        childRepository.save(childObject);

        // Save Parent
        CrudRepository parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        Method setRelationMethod = parentObject.getClass().getMethod(childSetterName, childObject.getClass());
        setRelationMethod.invoke(parentObject, childObject);
        parentRepository.save(parentObject);

        // Perform delete
        mockMvc
                .perform(delete(parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childAttributeName))
                .andExpect(status().isNoContent());

        objectValidator.assertToOneRelation(parentRepository, parentObject, null, childGetterName, true);
    }

    public void putOneToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childGetterName) throws Exception {
        // Save Parent
        CrudRepository parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        parentRepository.save(parentObject);

        // Save Child 1 & 2
        CrudRepository childRepository = oir.getRepository(childObjectDescription.getClassPath());

        List<Object> childObjects = new ArrayList<>();

        Object childObject1 = objectBuilder.buildObject(childObjectDescription);
        childRepository.save(childObject1);
        childObjects.add(childObject1);

        Object childObject2 = objectBuilder.buildObject(childObjectDescription);
        childRepository.save(childObject2);
        childObjects.add(childObject2);

        putObjects(parentObjectDescription.getRestPath(), parentObject, childObjects, childAttributeName);

        objectValidator.assertToManyRelation(parentRepository, parentObject, childObjects, childGetterName, true);
    }

    public void getAllOneToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childSetterName) throws Exception {
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);

        saveObjectWithRelation(parentObjectDescription.getClassPath(), childObjectDescription.getClassPath(), parentObject, childObjects, childSetterName);

        // Perform get
        ResultActions resultActions = mockMvc
                .perform(get(parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childAttributeName))
                .andExpect(status().isOk());

        for (int i = 0; i < childObjects.size(); i++) {
            String expectedId = oir.getId(childObjects.get(i)).toString();
            resultActions.andExpect(jsonPath("$[" + i + "].id").value(expectedId));
        }
    }

    public void getOneToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childSetterName) throws Exception {
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);

        saveObjectWithRelation(parentObjectDescription.getClassPath(), childObjectDescription.getClassPath(), parentObject, childObjects, childSetterName);

        // Perform get
        String expectedId = oir.getId(childObjects.get(0)).toString();
        mockMvc
                .perform(get(parentObjectDescription.getRestPath()
                        + "/" + oir.getId(parentObject)
                        + "/" + childAttributeName
                        + "/" + expectedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId));
    }

    public void deleteOneToManyTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, String childAttributeName, String childSetterName, String childGetterName) throws Exception {
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);

        saveObjectWithRelation(parentObjectDescription.getClassPath(), childObjectDescription.getClassPath(), parentObject, childObjects, childSetterName);

        // Perform delete
        String expectedId = oir.getId(childObjects.get(0)).toString();
        mockMvc
                .perform(delete(parentObjectDescription.getRestPath()
                        + "/" + oir.getId(parentObject)
                        + "/" + childAttributeName
                        + "/" + expectedId))
                .andExpect(status().isNoContent());

        childObjects.remove(0);
        objectValidator.assertToManyRelation(oir.getRepository(parentObjectDescription.getClassPath()), parentObject, childObjects, childGetterName, true);
    }

    private void putObjects(String restPath, Object parentObject, List<Object> childObjects, String childAttributeName) throws Exception {
        mockMvc
                .perform(put(restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObjects))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    private Object saveObjectWithRelation(String parentClassPath, String childClassPath, Object parentObject, List<Object> childObjects, String childSetterName) throws Exception {
        // Save Parent
        Class parentClass = Class.forName(parentClassPath);
        CrudRepository parentRepository = oir.getRepository(parentClassPath);
        parentRepository.save(parentObject);

        // Save Childs
        if (!childObjects.isEmpty()) {
            CrudRepository childRepository = oir.getRepository(childClassPath);
            Method setRelationMethod = parentClass.getMethod(childSetterName, List.class);

            for (Object child : childObjects) {
                childRepository.save(child);
            }

            setRelationMethod.invoke(parentObject, childObjects);
            parentRepository.save(parentObject);
        }

        return parentObject;
    }
}
