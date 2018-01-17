package Model;

public class AlignmentFactory {

    public static Alignment createAlignment(String line) {
        final String[] fields = line.split("\t");
        return new Alignment(fields);
    }

}
