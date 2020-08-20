package thkoeln.st.springtestlib.core.objectdescription;


public class ObjectDescriptionNotLoadable extends RuntimeException {

    private Exception e;


    public ObjectDescriptionNotLoadable(String name, Exception e) {
        super("ObjectDescription with name " + name + " could not be loaded", e);
    }
}

