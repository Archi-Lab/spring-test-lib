package thkoeln.st.springtestlib.core.objectdescription;

import thkoeln.st.springtestlib.core.Attribute;

import java.util.ArrayList;

/**
 * Describes an entity or value object in an abstract way
 * Contains all sorts of information that can be used to perform tests with reflection which are based on the described object
 */
public class ObjectDescription {
    private String className;
    private String classPath;
    private String dtoClassPath;
    private String restPath;

    private String getToOne;
    private String getToMany;
    private String setToOne;
    private String setToMany;

    private String attributeSingular;
    private String attributePlural;

    private Attribute[] attributes = new Attribute[]{};
    private Attribute[] invalidAttributes = new Attribute[]{};
    private Attribute[] updatedAttributes = new Attribute[]{};
    private Attribute[] hiddenAttributes = new Attribute[]{};

    private String serializedJson;


    public ObjectDescription() {}

    public void init() {
        for (Attribute attribute : attributes) {
            attribute.buildAttribute();
        }
        for (Attribute attribute : invalidAttributes) {
            attribute.buildAttribute();
        }
        for (Attribute attribute : updatedAttributes) {
            attribute.buildAttribute();
        }
        for (Attribute attribute : hiddenAttributes) {
            attribute.buildAttribute();
        }
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

    public String getClassName() {
        return className;
    }

    public String getDtoClassPath() {
        return dtoClassPath;
    }

    public String getRestPath() {
        return restPath;
    }

    public String getGetToOne() {
        return getToOne;
    }

    public String getGetToMany() {
        return getToMany;
    }

    public String getSetToOne() {
        return setToOne;
    }

    public String getSetToMany() {
        return setToMany;
    }

    public String getAttributeSingular() {
        return attributeSingular;
    }

    public String getAttributePlural() {
        return attributePlural;
    }

    public Attribute[] getInvalidAttributes() {
        return invalidAttributes;
    }

    public String getSerializedJson() {
        return serializedJson;
    }
}
