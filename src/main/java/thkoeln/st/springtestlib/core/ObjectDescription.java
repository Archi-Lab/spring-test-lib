package thkoeln.st.springtestlib.core;

public class ObjectDescription {
    private final String classPath;
    private String dtoClassPath;

    private String restPath;

    private Attribute[] attributes;
    private Attribute[] updatedAttributes;
    private Attribute[] hiddenAttributes;
    private String serializedJson;


    /**
     * Constructor for VOs
      */
    public ObjectDescription(String classPath, String dtoClassPath, Attribute[] attributes, Attribute[] hiddenAttributes, Attribute[] updatedAttributes) {
        this.classPath = classPath;
        this.dtoClassPath = dtoClassPath;
        this.attributes = attributes;
        this.hiddenAttributes = hiddenAttributes;
        this.updatedAttributes = updatedAttributes;
    }

    /**
     * Constructor for VOs
     */
    public ObjectDescription(String classPath, String dtoClassPath, String serializedJson) {
        this.classPath = classPath;
        this.dtoClassPath = dtoClassPath;
        this.serializedJson = serializedJson;
    }

    /**
     * Constructor for Entities
     */
    public ObjectDescription(String classPath, String dtoClassPath, String restPath, Attribute[] attributes, Attribute[] hiddenAttributes, Attribute[] updatedAttributes) {
        this.classPath = classPath;
        this.dtoClassPath = dtoClassPath;
        this.restPath = restPath;
        this.attributes = attributes;
        this.hiddenAttributes = hiddenAttributes;
        this.updatedAttributes = updatedAttributes;
    }

    /**
     * Constructor for Entities
     */
    public ObjectDescription(String classPath, String dtoClassPath, String restPath, String serializedJson) {
        this.classPath = classPath;
        this.dtoClassPath = dtoClassPath;
        this.restPath = restPath;
        this.serializedJson = serializedJson;
    }

    public String getClassPath() {
        return classPath;
    }

    public String getDtoClassPath() {
        return dtoClassPath;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public String getSerializedJson() {
        return serializedJson;
    }

    public Attribute[] getHiddenAttributes() {
        return hiddenAttributes;
    }

    public String getRestPath() {
        return restPath;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public void setHiddenAttributes(Attribute[] hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }

    public void setSerializedJson(String serializedJson) {
        this.serializedJson = serializedJson;
    }

    public Attribute[] getUpdatedAttributes() {
        return updatedAttributes;
    }

    public void setUpdatedAttributes(Attribute[] updatedAttributes) {
        this.updatedAttributes = updatedAttributes;
    }
}
