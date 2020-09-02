package thkoeln.st.springtestlib.core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Retrieves information from an object such as the corresponding repository or the id of an object
 */
public class ObjectInfoRetriever {

    private Repositories repositories = null;


    public ObjectInfoRetriever(WebApplicationContext appContext) {
        repositories = new Repositories(appContext);
    }

    public CrudRepository<Object, UUID> getRepository(String classPath) throws Exception {
        Class<?> clazz = Class.forName(classPath);
        return (CrudRepository<Object, UUID>) repositories.getRepositoryFor(clazz).get();
    }

    public UUID getId(Object object) throws Exception {
        Method getIdMethod = object.getClass().getMethod("getId");
        return (UUID) getIdMethod.invoke(object);
    }
}
