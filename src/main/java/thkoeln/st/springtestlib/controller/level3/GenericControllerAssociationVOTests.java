package thkoeln.st.springtestlib.controller.level3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.Link;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;
import thkoeln.st.springtestlib.core.objectdescription.RESTObjectDescription;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GenericControllerAssociationVOTests extends GenericTests {

    private static final int COLLECTION_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerAssociationVOTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public Object putOneToOneVOTest(Object parentObject, RESTObjectDescription parentObjectDescription, RESTObjectDescription childObjectDescription, String childAttributeName, String childGetterName, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);
        }

        // Create Child
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        ResultActions resultActions = putChild(parentObjectDescription.getRestPath(), parentObject, childObject, childAttributeName);

        // Test Fields
        objectValidator.validateResultActions(childObject, resultActions, childObjectDescription.getAttributes(), new Attribute[]{}, "." + childAttributeName);
        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");

        objectValidator.assertToOneRelation(parentRepository, parentObject, childObject, childGetterName, false);
        return childObject;
    }

    public Object postOneToManyVOTest(Object parentObject, RESTObjectDescription parentObjectDescription, RESTObjectDescription childObjectDescription, String childAttributeName, String childGetterName, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);
        }

        // Create Children
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        ResultActions resultActions = postChildToCollection(parentObjectDescription.getRestPath(), parentObject, childObject, childAttributeName);

        // Test Fields
        String preIdentifier = "." + childAttributeName + "[0]";
        objectValidator.validateResultActions(childObject, resultActions, childObjectDescription.getAttributes(), new Attribute[]{}, preIdentifier);

        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");
        objectValidator.assertToManyRelation(parentRepository, parentObject, Collections.singletonList(childObject), childGetterName, false);
        return childObject;
    }

    public void deleteOneToManyVOTest(Object parentObject, RESTObjectDescription parentObjectDescription, RESTObjectDescription childObjectDescription, String childAttributeName, String childGetterName, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);

            List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);
            Field childField = parentObject.getClass().getDeclaredField(childAttributeName);
            childField.setAccessible(true);
            childField.set(parentObject, childObjects);
        }

        ResultActions resultActions = mockMvc
            .perform(delete(parentObjectDescription.getRestPath() + "/" + oir.getId(parentObject) + "/" + childAttributeName))
            .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");
        objectValidator.assertToManyRelation(parentRepository, parentObject, new ArrayList<>() {}, childGetterName, false);
    }

    private ResultActions putChild(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(put(restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ResultActions postChildToCollection(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(post(restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
