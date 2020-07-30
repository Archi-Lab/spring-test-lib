package thkoeln.st.springtestlib.testing;

public class Attribute {

    private final String name;
    private final String type;
    private final String serializedValue;
    private final Object value;


    public Attribute(String name, String type, String serializedValue) {
        this.name = name;
        this.type = type;
        this.serializedValue = serializedValue;
        this.value = buildAttribute();
    }

    private Object buildAttribute() {
        if (serializedValue.equals("null")) {
            return null;
        }

        switch (type) {
            case "Integer":
                return Integer.parseInt(serializedValue);
            case "Float":
                return Float.parseFloat(serializedValue);
            case "String":
                return serializedValue;
        }

        throw new IllegalArgumentException("Attribute " + name + " has incorrect type " + type);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getSerializedValue() {
        return serializedValue;
    }
}
