package Model;

public class VariantFactory {
    public static Variant createVariant(String line) {
        if (line.startsWith("#")) return null;
        final String[] fields = line.split("\t");
        Variant variant = new Variant(fields);
        String[] genotype = fields[8].split("/");
        if (genotype.length == 1) genotype = fields[8].split("\\|");
        if (genotype[0].equals(genotype[1]))
            variant.setGENOTYPE("HOM");
        else variant.setGENOTYPE("HET");
        return variant;
    }
}
