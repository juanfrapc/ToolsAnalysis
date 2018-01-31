package Parser;

import Model.Alignment;
import Model.AlignmentsStatistics;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class ParseFileStats {

    public static boolean process(File file, AlignmentsStatistics stats) {

        Parser parser = new Parser();
        Stream<Alignment> stream;
        try {
            stream = file.getName().endsWith("sam") ? parser.parseSam(file) : parser.parseBam(file);
            stream.parallel().forEach(alignment -> {if (alignment != null) stats.update(alignment);});
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }



}
