package thkoeln.st.springtestlib.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectBuilder {

    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * deprecated
     * @param classPath
     * @param attributes
     * @return
     * @throws Exception
     */
    public Object buildObject(String classPath, Attribute[] attributes) throws Exception {
        Class clazz = Class.forName(classPath);
        Object object = clazz.getConstructor().newInstance();

        setObjectFieldValues(object, attributes);
        return object;
    }

    public Object buildObject(ObjectDescription objectDescription) throws Exception {
        Class clazz = Class.forName(objectDescription.getClassPath());
        Object object = clazz.getConstructor().newInstance();

        if (objectDescription.getAttributes() == null) {
            setObjectFieldValues(object, objectDescription.getSerializedJson());
        } else {
            setObjectFieldValues(object, objectDescription.getAttributes());
        }
        return object;
    }

    public Object buildInvalidObject(ObjectDescription objectDescription) throws Exception {
        Class clazz = Class.forName(objectDescription.getClassPath());
        Object object = clazz.getConstructor().newInstance();

        setObjectFieldValues(object, objectDescription.getInvalidAttributes());
        return object;
    }

    public void setObjectFieldValues(Object object, String serializedJson) throws Exception {
        Object newValuesObject = objectMapper.readValue(serializedJson, object.getClass());

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            field.set(object, field.get(newValuesObject));
        }
    }

    public void setObjectFieldValues(Object object, Attribute[] attributes) throws Exception {
        List<Attribute> attributeList = new ArrayList<>(Arrays.asList(attributes));

        for (Field field : object.getClass().getDeclaredFields()) {
            if (isValueObject(field)) {
                Object valueObject = field.getType().getConstructor().newInstance();
                for (Field valueObjectField : valueObject.getClass().getDeclaredFields()) {
                    setAttribute(valueObject, valueObjectField, attributeList);
                }

                field.setAccessible(true);
                field.set(object, valueObject);
            } else {
                setAttribute(object, field, attributeList);
            }
        }

        if (!attributeList.isEmpty()) {
            String missing = "";
            for (Attribute unusedAttribute : attributeList) {
                System.out.println("Unused Attribute: " + unusedAttribute.getName());
                missing += unusedAttribute.getName();
            }
            throw new Exception( "There are Attributes that seem to be missing from '"
                    + object.getClass().getName() + " or an embedded class': '" + missing +
                    "'. Did you forget to declare the attributes in your entity or value object?" );
        }
    }

    public boolean isValueObject(Field field) {
        Annotation[] valueObjectAnnotations = field.getType().getAnnotations();
        for (Annotation annotation : valueObjectAnnotations) {
            if (annotation.toString().equals("@javax.persistence.Embeddable()")) {
                return true;
            }
        }

        return false;
    }

    private void setAttribute(Object object, Field field, List<Attribute> attributeList) throws Exception {
        Attribute attribute = getAttributeByName(attributeList, field.getName());

        if (attribute != null) {
            field.setAccessible(true);
            field.set(object, attribute.getValue());
        }
    }

    private Attribute getAttributeByName(List<Attribute> attributeList, String name) {
        for (int i = 0; i < attributeList.size(); i++) {
            Attribute attribute = attributeList.get(i);
            if (attribute.getName().equals(name)) {
                attributeList.remove(i);
                return attribute;
            }
        }

        return null;
    }

    /**
     * deprecated
     * @param classPath
     * @param attributes
     * @param count
     * @return
     * @throws Exception
     */
    public List<Object> buildObjectList(String classPath, Attribute[] attributes, int count) throws Exception {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Object object = buildObject(classPath, attributes);
            objects.add(object);
        }
        return objects;
    }

    public List<Object> buildObjectList(ObjectDescription objectDescription, int count) throws Exception {
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Object object = buildObject(objectDescription);
            objects.add(object);
        }
        return objects;
    }
}
