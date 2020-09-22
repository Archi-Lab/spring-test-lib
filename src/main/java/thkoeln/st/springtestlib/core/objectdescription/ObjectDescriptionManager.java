package thkoeln.st.springtestlib.core.objectdescription;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and manages multiple descriptions of different entities or value objects
 */
public class ObjectDescriptionManager {

    private static ObjectDescriptionManager instance;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, ObjectDescription> objectDescriptions = new HashMap<>();


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

    private String getObjectDescriptionFolderPath () {
        ClassLoader loader = getClass().getClassLoader();
        URL url = loader.getResource(".");
        if (url == null) {
            throw new RuntimeException("Could not load resources folder");
        }

        File[] files = new File(url.getPath()).listFiles();
        if (files == null) {
            throw new RuntimeException("Could not load files inside the resources folder");
        }

        for (File file : files) {
            if (file.getName().contains("objectdescriptions")) {
                return file.getName();
            }
        }

        throw new RuntimeException("Could not find folder with objectdescriptions");
    }

    private ObjectDescription loadObjectDescription(String className) {
        String objectDescriptionFolder = getObjectDescriptionFolderPath();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(objectDescriptionFolder + "/" + className + ".json");

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
