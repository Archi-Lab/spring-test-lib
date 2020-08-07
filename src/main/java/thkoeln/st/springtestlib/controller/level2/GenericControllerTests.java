package thkoeln.st.springtestlib.controller.level2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;
import thkoeln.st.springtestlib.core.objectdescription.RESTObjectDescription;

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

    public void getTest(RESTObjectDescription objectDescription) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(objectDescription.getClassPath());
        Object object = objectBuilder.buildObject(objectDescription);
        repository.save(object);

        // Perform get
        ResultActions resultActions = mockMvc
                .perform(get(objectDescription.getRestPath() + "/" + oir.getId(object)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActions(object, resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "");
    }

    public void getAllTest(RESTObjectDescription objectDescription) throws Exception {
        // Save Object List
        CrudRepository repository = oir.getRepository(objectDescription.getClassPath());
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
            objectValidator.validateResultActions(objectList.get(i), resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "[" + i + "]");
        }
    }

    public void postTest(RESTObjectDescription objectDescription) throws Exception {
        // Create Object
        Object object = objectBuilder.buildObject(objectDescription);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(objectDescription.getRestPath())
                                .content(objectMapper.writeValueAsString(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        objectValidator.validateResultActions(object, resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(object, retrievedObject, objectDescription.getAttributes());
    }

    public void putTest(RESTObjectDescription objectDescription) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(objectDescription.getClassPath());
        Object object = objectBuilder.buildObject(objectDescription);
        repository.save(object);

        // Change object
        objectBuilder.setObjectFieldValues(object, objectDescription.getUpdatedAttributes());

        // Perform put
        mockMvc
                .perform(
                        put(objectDescription.getRestPath() + "/" + oir.getId(object))
                                .content(objectMapper.writeValueAsString(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(object, retrievedObject, objectDescription.getAttributes());
    }

    public void deleteTest(RESTObjectDescription objectDescription) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(objectDescription.getClassPath());
        Object object = objectBuilder.buildObject(objectDescription);
        repository.save(object);

        // Perform delete
        mockMvc
                .perform(delete(objectDescription.getRestPath() + "/" + oir.getId(object)))
                .andExpect(status().isNoContent());

        Optional<Object> objectOp = repository.findById(oir.getId(object));
        assertFalse(objectOp.isPresent());
    }

    public void noRestLevel3Test(RESTObjectDescription objectDescription) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(objectDescription.getClassPath());
        Object object = objectBuilder.buildObject(objectDescription);
        repository.save(object);

        // Perform get
        MockHttpServletResponse getResponse = mockMvc
                .perform(get(objectDescription.getRestPath() + "/" + oir.getId(object)).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertTrue(
                getResponse.getStatus() == HttpStatus.NOT_FOUND.value()
                        || !getResponse.getContentAsString().contains("_links"));

        // Perform getAll
        MockHttpServletResponse getAllResponse = mockMvc
                .perform(get(objectDescription.getRestPath()).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertTrue(
                getAllResponse.getStatus() == HttpStatus.NOT_FOUND.value()
                        || !getAllResponse.getContentAsString().contains("_links"));
    }
}
