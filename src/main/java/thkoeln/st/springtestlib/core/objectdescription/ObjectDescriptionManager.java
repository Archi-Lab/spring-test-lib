package thkoeln.st.springtestlib.core.objectdescription;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class loads and manages multiple descriptions of different entities or value objects
 */
public class ObjectDescriptionManager {

    private static ObjectDescriptionManager instance;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, ObjectDescription> objectDescriptions = new HashMap<>();


    public static ObjectDescriptionManager getInstance() {
        if (instance == null) {
            instance = new ObjectDescriptionManager();
        }
        return instance;
    }

    /**
     * Loads and caches a specific object description which should be located under "resources/objectdescriptions"
     * @param className className of the objectDescription which should be returned
     * @return objectDescription which was found under the given className
     */
    public ObjectDescription getObjectDescription(String className) {
        ObjectDescription foundObjectDescription = objectDescriptions.get(className);
        if (foundObjectDescription == null) {
            foundObjectDescription = loadObjectDescription(className);
            foundObjectDescription.init();
            objectDescriptions.put(className, foundObjectDescription);
        }
        return foundObjectDescription;
    }

    private ObjectDescription loadObjectDescription(String className) {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("objectdescriptions/" + className + ".json");
        if (resource == null) {
            throw new ObjectDescriptionNotFoundException(className);
        } else {
            File resourceFile = new File(resource.getFile());
            try {
                return objectMapper.readValue(resourceFile, ObjectDescription.class);
            } catch (IOException e) {
                throw new ObjectDescriptionNotLoadable(className, e);
            }
        }
    }
}
