package Model;

public class AlignmentFactory {

    public static Alignment createAligment(String line) {
        final String[] fields = line.split("\t");
        String name = fields[0];
        Integer.valueOf(fields[1]);
        return new Alignment();
    }

}
