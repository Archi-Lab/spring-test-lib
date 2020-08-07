package thkoeln.st.springtestlib.core.objectdescription;

import thkoeln.st.springtestlib.core.Attribute;

import java.util.ArrayList;

public class ObjectDescription {
    protected final String classPath;

    protected Attribute[] attributes;
    protected Attribute[] updatedAttributes;
    protected Attribute[] hiddenAttributes;
    protected String serializedJson;


    public ObjectDescription(String classPath, Attribute[] attributes, Attribute[] hiddenAttributes, Attribute[] updatedAttributes) {
        this.classPath = classPath;
        this.attributes = attributes;
        this.updatedAttributes = updatedAttributes;
        this.hiddenAttributes = hiddenAttributes;
    }

    public ObjectDescription(String classPath, Attribute[] attributes) {
        this.classPath = classPath;
        this.attributes = attributes;
        this.hiddenAttributes = new Attribute[]{};
        this.updatedAttributes = new Attribute[]{};
    }

    public ObjectDescription(String classPath, String serializedJson) {
        this.classPath = classPath;
        this.serializedJson = serializedJson;
    }

    public String getClassPath() {
        return classPath;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public Attribute[] getUpdatedAttributes() {
        return updatedAttributes;
    }

    public Attribute[] getHiddenAttributes() {
        return hiddenAttributes;
    }

    public String getSerializedJson() {
        return serializedJson;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public void setUpdatedAttributes(Attribute[] updatedAttributes) {
        this.updatedAttributes = updatedAttributes;
    }

    public void setHiddenAttributes(Attribute[] hiddenAttributes) {
        this.hiddenAttributes = hiddenAttributes;
    }

    public void setSerializedJson(String serializedJson) {
        this.serializedJson = serializedJson;
    }
}
