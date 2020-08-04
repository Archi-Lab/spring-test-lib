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
import thkoeln.st.springtestlib.core.ObjectDescription;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GenericControllerTests extends GenericTests {

    private static final int GET_ALL_TEST_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public Object getTest(Object expectedObject, ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        if (expectedObject == null) {
            CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
            expectedObject = objectBuilder.buildObject(objectDescription);
            repository.save(expectedObject);
        }

        ResultActions resultActions = mockMvc
                .perform(get(objectDescription.getRestPath() + "/" + oir.getId(expectedObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActions(expectedObject, resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "");
        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Class<?> dtoClass = Class.forName(objectDescription.getDtoClassPath());
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), dtoClass);
    }

    public void getAllTest(ObjectDescription objectDescription, String attributeNamePlural, Link[] expectedLinks, Link[] hiddenLinks, Link collectionSelfLink) throws Exception {
        // Save Object List
        CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
        List<Object> objectList = objectBuilder.buildObjectList(objectDescription, GET_ALL_TEST_COUNT);
        for (Object object : objectList) {
            repository.save(object);
        }

        // Perform getAll
        ResultActions resultActions = mockMvc
                .perform(get(objectDescription.getRestPath()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Test Fields
        for (int i = 0; i < GET_ALL_TEST_COUNT; i++) {
            String preIdentifier = "._embedded." + attributeNamePlural + "[" + i + "]";
            objectValidator.validateResultActions(objectList.get(i), resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), preIdentifier);
            objectValidator.validateResultActionLinks(Collections.singletonList(objectList.get(i)), resultActions, expectedLinks, hiddenLinks, "._embedded." + attributeNamePlural + "[" + i + "]");
        }

        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, new Link[]{collectionSelfLink}, new Link[]{}, "");
    }

    public Object postTest(ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        Attribute[] diffAttributes = getAttributeDiff(objectDescription.getAttributes(), objectDescription.getHiddenAttributes());
        objectDescription.setAttributes(diffAttributes);

        Object expectedObject = objectBuilder.buildObject(objectDescription);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(objectDescription.getRestPath())
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        objectValidator.validateResultActions(expectedObject, resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "");
        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, objectDescription.getAttributes());
        return retrievedObject;
    }

    public void putTest(Object expectedObject, ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        if (expectedObject == null) {
            CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
            expectedObject = objectBuilder.buildObject(objectDescription);
            repository.save(expectedObject);
        }

        objectBuilder.setObjectFieldValues(expectedObject, objectDescription.getUpdatedAttributes());

        ResultActions resultActions = mockMvc
                .perform(
                        put(objectDescription.getRestPath() + "/" + oir.getId(expectedObject))
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, objectDescription.getAttributes());
    }

    public void deleteTest(ObjectDescription objectDescription) throws Exception {
        // Save Object
        CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
        Object object = objectBuilder.buildObject(objectDescription);
        repository.save(object);

        // Perform delete
        mockMvc
                .perform(delete(objectDescription.getRestPath() + "/" + oir.getId(object)))
                .andExpect(status().isNoContent());

        Optional<Object> objectOp = repository.findById(oir.getId(object));
        assertFalse(objectOp.isPresent());
    }

    private Attribute[] getAttributeDiff(Attribute[] attributes, Attribute[] hiddenAttributes) {
        Set<Attribute> allAttributeSet = new HashSet<>();
        Collections.addAll(allAttributeSet, attributes);
        Set<Attribute> hiddenAttributeSet = new HashSet<>();
        Collections.addAll( hiddenAttributeSet, hiddenAttributes );
        allAttributeSet.removeAll( hiddenAttributeSet );
        return allAttributeSet.toArray(new Attribute[0]);
    }
}
