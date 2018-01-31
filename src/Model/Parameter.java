package Model;

public class Parameter {

    private char name;
    private String value;

    public Parameter(char name, String value) {
        this.name = name;
        this.value = value;
    }

    public Parameter(String value){
        this((char) 0, value);
    }

    public char getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String ret = ((name == 0) ? "" : ("-" + name + " "));
        ret += value;
        return ret;
    }
}
