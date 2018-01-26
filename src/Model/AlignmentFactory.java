package Model;

public class AlignmentFactory {

    public static Alignment createAlignment(String line) {
        if (line.startsWith("@")) return null;
        final String[] fields = line.split("\t");
        return new Alignment(fields);
    }

}
