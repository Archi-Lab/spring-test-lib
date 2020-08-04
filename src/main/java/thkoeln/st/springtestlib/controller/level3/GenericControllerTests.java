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

    public Object getTest(Object expectedObject, String restPath, String classPath, String dtoClassPath, Attribute[] attributes, Attribute[] hiddenAttributes, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        if (expectedObject == null) {
            CrudRepository<Object, UUID> repository = oir.getRepository(classPath);
            expectedObject = objectBuilder.buildObject(classPath, attributes);
            repository.save(expectedObject);
        }

        ResultActions resultActions = mockMvc
                .perform(get(restPath + "/" + oir.getId(expectedObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActions(expectedObject, resultActions, attributes, hiddenAttributes, "");
        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Class<?> dtoClass = Class.forName(dtoClassPath);
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), dtoClass);
    }

    public void getAllTest(String restPath, String classPath, String attributeNamePlural, Attribute[] attributes, Attribute[] hiddenAttributes, Link[] expectedLinks, Link[] hiddenLinks, Link collectionSelfLink) throws Exception {
        // Save Object List
        CrudRepository<Object, UUID> repository = oir.getRepository(classPath);
        List<Object> objectList = objectBuilder.buildObjectList(classPath, attributes, GET_ALL_TEST_COUNT);
        for (Object object : objectList) {
            repository.save(object);
        }

        // Perform getAll
        ResultActions resultActions = mockMvc
                .perform(get(restPath).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Test Fields
        for (int i = 0; i < GET_ALL_TEST_COUNT; i++) {
            String preIdentifier = "._embedded." + attributeNamePlural + "[" + i + "]";
            objectValidator.validateResultActions(objectList.get(i), resultActions, attributes, hiddenAttributes, preIdentifier);
            objectValidator.validateResultActionLinks(Collections.singletonList(objectList.get(i)), resultActions, expectedLinks, hiddenLinks, "._embedded." + attributeNamePlural + "[" + i + "]");
        }

        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, new Link[]{collectionSelfLink}, new Link[]{}, "");
    }

    public Object postTest(String restPath, String classPath, Attribute[] attributes, Attribute[] hiddenAttributes, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        Attribute[] diffAttributes = getAttributeDiff(attributes, hiddenAttributes);

        Object expectedObject = objectBuilder.buildObject(classPath, diffAttributes);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(restPath)
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        objectValidator.validateResultActions(expectedObject, resultActions, attributes, hiddenAttributes, "");
        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(classPath).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, attributes);
        return retrievedObject;
    }

    public void putTest(Object expectedObject, String restPath, String classPath, Attribute[] attributes, Attribute[] newAttributes, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        if (expectedObject == null) {
            CrudRepository<Object, UUID> repository = oir.getRepository(classPath);
            expectedObject = objectBuilder.buildObject(classPath, attributes);
            repository.save(expectedObject);
        }

        objectBuilder.setObjectFieldValues(expectedObject, newAttributes);

        ResultActions resultActions = mockMvc
                .perform(
                        put(restPath + "/" + oir.getId(expectedObject))
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(classPath).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, attributes);
    }

    public void deleteTest(String restPath, String classPath, Attribute[] attributes) throws Exception {
        // Save Object
        CrudRepository<Object, UUID> repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform delete
        mockMvc
                .perform(delete(restPath + "/" + oir.getId(object)))
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
