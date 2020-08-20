package thkoeln.st.springtestlib.controller.level2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenericControllerAssociationVOTests extends GenericTests {

    private static final String BASE_PATH = "/level-2";
    private static final int COLLECTION_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerAssociationVOTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public void getOneToOneVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Create Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);

        // Create Child
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributeSingular());
        childField.setAccessible(true);
        childField.set(parentObject, childObject);
        parentRepository.save(parentObject);

        mockMvc
            .perform(get(BASE_PATH + parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject)))
            .andExpect(jsonPath(childObjectDescription.getAttributeSingular()).exists())
            .andExpect(status().isOk());
    }

    public Object putOneToOneVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        parentRepository.save(parentObject);

        // Create Child
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        putChild(parentObjectDescription.getRestPath(), parentObject, childObject, childObjectDescription.getAttributeSingular());

        objectValidator.assertToOneRelation(parentRepository, parentObject, childObject, childObjectDescription.getGetToOne(), false);
        return childObject;
    }

    public void getOneToManyVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Create Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);

        // Create Children
        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);
        Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributePlural());
        childField.setAccessible(true);
        childField.set(parentObject, childObjects);
        parentRepository.save(parentObject);

        mockMvc
                .perform(get(BASE_PATH + parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject)))
                .andExpect(jsonPath(childObjectDescription.getAttributePlural()).exists())
                .andExpect(status().isOk());
    }

    public Object postOneToManyVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        parentRepository.save(parentObject);

        // Create Children
        Object childObject = objectBuilder.buildObject(childObjectDescription);
        ResultActions resultActions = postChildToCollection(parentObjectDescription.getRestPath(), parentObject, childObject, childObjectDescription.getAttributePlural());

        // Test Fields
        String preIdentifier = "." + childObjectDescription.getAttributePlural() + "[0]";
        objectValidator.validateResultActions(childObject, resultActions, childObjectDescription.getAttributes(), new Attribute[]{}, preIdentifier);

        objectValidator.assertToManyRelation(parentRepository, parentObject, Collections.singletonList(childObject), childObjectDescription.getGetToMany(), false);
        return childObject;
    }

    public void deleteOneToManyVOTest(ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        Object parentObject = objectBuilder.buildObject(parentObjectDescription);
        parentRepository.save(parentObject);

        List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);
        Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributePlural());
        childField.setAccessible(true);
        childField.set(parentObject, childObjects);

        ResultActions resultActions = mockMvc
            .perform(delete(BASE_PATH + parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childObjectDescription.getAttributePlural()))
            .andExpect(status().isOk());

        objectValidator.assertToManyRelation(parentRepository, parentObject, new ArrayList<>() {}, childObjectDescription.getGetToMany(), false);
    }

    private ResultActions putChild(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(put(BASE_PATH + restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ResultActions postChildToCollection(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(post(BASE_PATH + restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
