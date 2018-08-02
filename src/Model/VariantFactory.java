package Model;

public class VariantFactory {
    public static Variant createvariant(String line) {
        if (line.startsWith("#")) return null;
        final String[] fields = line.split("\t");
        Variant variant = new Variant(fields);
        if (fields[10].contains("0/0") || fields[10].contains("0|0") || fields[10].contains("1/1") || fields[10].contains("1|1"))
            variant.setGENOTYPE("HOM");
        else variant.setGENOTYPE("HET");
        return variant;
    }
}
