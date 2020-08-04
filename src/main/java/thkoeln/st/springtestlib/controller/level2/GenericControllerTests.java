package thkoeln.st.springtestlib.controller.level2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    public void getTest(String restPath, String classPath, Attribute[] attributes, Attribute[] hiddenAttributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform get
        ResultActions resultActions = mockMvc
                .perform(get(restPath + "/" + oir.getId(object)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActions(object, resultActions, attributes, hiddenAttributes, "");
    }

    public void getAllTest(String restPath, String classPath, Attribute[] attributes, Attribute[] hiddenAttributes) throws Exception {
        // Save Object List
        CrudRepository repository = oir.getRepository(classPath);
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
            objectValidator.validateResultActions(objectList.get(i), resultActions, attributes, hiddenAttributes, "[" + i + "]");
        }
    }

    public void postTest(String restPath, String classPath, Attribute[] attributes, Attribute[] hiddenAttributes) throws Exception {
        // Create Object
        Object object = objectBuilder.buildObject(classPath, attributes);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(restPath)
                                .content(objectMapper.writeValueAsString(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        objectValidator.validateResultActions(object, resultActions, attributes, hiddenAttributes, "");

        Object retrievedObject = oir.getRepository(classPath).findAll().iterator().next();
        objectValidator.validateTwoObjects(object, retrievedObject, attributes);
    }

    public void putTest(String restPath, String classPath, Attribute[] attributes, Attribute[] newAttributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Change object
        objectBuilder.setObjectFieldValues(object, newAttributes);

        // Perform put
        mockMvc
                .perform(
                        put(restPath + "/" + oir.getId(object))
                                .content(objectMapper.writeValueAsString(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Object retrievedObject = oir.getRepository(classPath).findAll().iterator().next();
        objectValidator.validateTwoObjects(object, retrievedObject, attributes);
    }

    public void deleteTest(String restPath, String classPath, Attribute[] attributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform delete
        mockMvc
                .perform(delete(restPath + "/" + oir.getId(object)))
                .andExpect(status().isNoContent());

        Optional<Object> objectOp = repository.findById(oir.getId(object));
        assertFalse(objectOp.isPresent());
    }

    public void noRestLevel3Test(String restPath, String classPath, Attribute[] attributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform get
        MockHttpServletResponse getResponse = mockMvc
                .perform(get(restPath + "/" + oir.getId(object)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertTrue(
                getResponse.getStatus() == HttpStatus.NOT_FOUND.value()
                        || !getResponse.getContentAsString().contains("_links"));

        // Perform getAll
        MockHttpServletResponse getAllResponse = mockMvc
                .perform(get(restPath).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertTrue(
                getAllResponse.getStatus() == HttpStatus.NOT_FOUND.value()
                        || !getAllResponse.getContentAsString().contains("_links"));
    }
}
