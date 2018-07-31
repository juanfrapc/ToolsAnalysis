package Model;

import java.util.Random;

public class Parameter implements Cloneable {

    private final char name;
    private String value;

    public Parameter(char name, String value) {
        this.name = name;
        this.value = value;
    }

    public Parameter(String value) {
        this((char) 0, value);
    }

    public char getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = this.name == 'r' ? value : ((int) Double.parseDouble(value)) + "";
    }

    @Override
    public String toString() {
        String ret = ((name == 0) ? "" : ("-" + name + " "));
        ret += value;
        return ret;
    }

    @Override
    public Parameter clone() {
        return new Parameter(this.name, this.value);
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
        //int result = (int) getName();
        //result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        try {
            return Integer.parseInt(getValue());
        }catch (NumberFormatException e){
            return (int)Float.parseFloat(getValue())*100;
        }
    }

    public void updateRandom(char[] floats, char[] negatives) {
        Random random = new Random();
        double old = Double.parseDouble(getValue());
        double v = random.nextGaussian();
        if (!contains(floats, getName())) {
            v = Math.ceil(v);
        }
        double ne = old + v < 0 && !contains(negatives, getName()) ? 1 : old + v;
        setValue(ne + "");
    }

    private boolean contains(char[] array, char value) {
        for (char c : array) {
            if (c == value) return true;
        }
        return false;
    }
}
