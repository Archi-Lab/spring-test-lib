package thkoeln.st.springtestlib.core;

import org.springframework.web.context.WebApplicationContext;

/**
 * This class is the foundation for all Generic Test Classes and contains useful instances for building, validating and retrieving information from entities and value objects
 */
public abstract class GenericTests {

    protected ObjectBuilder objectBuilder;
    protected ObjectValidator objectValidator;
    protected ObjectInfoRetriever oir;


    public GenericTests(WebApplicationContext appContext) {
        oir = new ObjectInfoRetriever(appContext);
        this.objectBuilder = new ObjectBuilder();
        this.objectValidator = new ObjectValidator(objectBuilder, oir);
    }
}
