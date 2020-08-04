package thkoeln.st.springtestlib.controller.level2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GenericControllerReturnCodeTest extends GenericTests {

    private static final int GET_ALL_TEST_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerReturnCodeTest(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    //Falsche URI, 404 erwartet
    public void wrongUriTest(String WrongRestPath, String classPath, Attribute[] attributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform get
        ResultActions resultActions = mockMvc
                .perform(get(WrongRestPath + "/" + oir.getId(object)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //Falsche ID, 404 erwartet
    public void wrongIDTest(String restPath, String classPath, Attribute[] attributes) throws Exception {
        // Perform get
        ResultActions resultActions = mockMvc
                .perform(get(restPath + "/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //Delete nicht erlaubt, 405 erwartet
    public void deleteMethodNotAllowedTest(String restPath, String classPath, Attribute[] attributes) throws Exception {
        // Save Object
        CrudRepository repository = oir.getRepository(classPath);
        Object object = objectBuilder.buildObject(classPath, attributes);
        repository.save(object);

        // Perform delete
        mockMvc
                .perform(delete(restPath + "/" + oir.getId(object)))
                .andExpect(status().isMethodNotAllowed());

        Optional<Object> objectOp = repository.findById(oir.getId(object));
        assertTrue(objectOp.isPresent());
    }

    public void invalidValueTest(String restPath, String classPath, Attribute[] attributes, Attribute[] hiddenAttributes) throws Exception {
        // Create Object
        Object object = objectBuilder.buildObject(classPath, attributes);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(restPath)
                                .content(objectMapper.writeValueAsString(object))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }
}
