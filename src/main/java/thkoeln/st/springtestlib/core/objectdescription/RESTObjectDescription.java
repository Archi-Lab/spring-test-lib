package thkoeln.st.springtestlib.core.objectdescription;

import thkoeln.st.springtestlib.core.Attribute;

public class RESTObjectDescription extends ObjectDescription {

    private String dtoClassPath;
    private String restPath;


    public RESTObjectDescription(String classPath, String dtoClassPath, String restPath, Attribute[] attributes, Attribute[] hiddenAttributes, Attribute[] updatedAttributes) {
        super(classPath, attributes, hiddenAttributes, updatedAttributes);
        this.dtoClassPath = dtoClassPath;
        this.restPath = restPath;
    }

    public RESTObjectDescription(String classPath, String dtoClassPath, String restPath, Attribute[] attributes) {
        super(classPath, attributes);
        this.dtoClassPath = dtoClassPath;
        this.restPath = restPath;
    }

    public RESTObjectDescription(String classPath, String dtoClassPath, String restPath, String serializedJson) {
        super(classPath, serializedJson);
        this.dtoClassPath = dtoClassPath;
        this.restPath = restPath;
    }

    public String getDtoClassPath() {
        return dtoClassPath;
    }

    public String getRestPath() {
        return restPath;
    }
}
