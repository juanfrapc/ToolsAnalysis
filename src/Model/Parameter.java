package Model;

public class Parameter implements Cloneable{

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
        this.value = this.name=='r'? value: ((int) Double.parseDouble(value)) + "";
    }

    @Override
    public String toString() {
        String ret = ((name == 0) ? "" : ("-" + name + " "));
        ret += value;
        return ret;
    }

    @Override
    public Parameter clone() throws CloneNotSupportedException {
        return (Parameter) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;

        Parameter parameter = (Parameter) o;

        return getName() == parameter.getName() && (getValue() != null ?
                getValue().equals(parameter.getValue()) : parameter.getValue() == null);
    }

    @Override
    public int hashCode() {
        int result = (int) getName();
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
