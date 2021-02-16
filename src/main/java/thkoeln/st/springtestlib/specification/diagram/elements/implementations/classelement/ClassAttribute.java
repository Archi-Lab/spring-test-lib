package thkoeln.st.springtestlib.specification.diagram.elements.implementations.classelement;

public class ClassAttribute {

    private String name;
    private String type;


    public ClassAttribute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClassAttribute)) {
            return false;
        }

        ClassAttribute other = (ClassAttribute)obj;
        boolean nameEquals = name == null || other.getName() == null
            ? name == null && other.getName() == null
            : name.equalsIgnoreCase(other.getName());

        boolean typeEquals = type == null || other.getType() == null
            ? type == null && other.getType() == null
            : type.equalsIgnoreCase(other.getType());

        return nameEquals && typeEquals;
    }
}
