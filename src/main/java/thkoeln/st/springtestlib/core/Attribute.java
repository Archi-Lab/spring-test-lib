package thkoeln.st.springtestlib.core;

public class Attribute {

    private String name;
    private String type;
    private String serializedValue;
    private Object value;


    public Attribute() {}

    public Attribute(String name, String type, String serializedValue) {
        this.name = name;
        this.type = type;
        this.serializedValue = serializedValue;
        buildAttribute();
    }

    public void buildAttribute() {
        if (serializedValue.equals("null")) {
            value = null;
        }

        switch (type) {
            case "Integer":
                value = Integer.parseInt(serializedValue);
                break;
            case "Float":
                value = Float.parseFloat(serializedValue);
                break;
            case "String":
                value = serializedValue;
                break;
            default:
                throw new IllegalArgumentException("Attribute " + name + " has incorrect type " + type);
        }
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
