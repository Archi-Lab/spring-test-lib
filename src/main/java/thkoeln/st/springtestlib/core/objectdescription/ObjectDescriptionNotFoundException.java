package thkoeln.st.springtestlib.core.objectdescription;


public class ObjectDescriptionNotFoundException extends RuntimeException {

    public ObjectDescriptionNotFoundException(String name) {
        super("ObjectDescription with name " + name + " was not found");
    }
}

