package thkoeln.st.springtestlib.core;

import org.springframework.web.context.WebApplicationContext;

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
