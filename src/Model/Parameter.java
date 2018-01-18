package Model;

public class Parameter {

    private char name;
    private Object value;

    public Parameter(char name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        String ret = name == 0 ? "" : "-" + name+ " ";
        ret += value;
        return ret;
    }
}
