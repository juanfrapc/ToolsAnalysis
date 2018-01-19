package Application;

import Model.Alignment;
import Model.AlignmentStatistics;
import Parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class ProcessFileStats {

    public static boolean process(File file, AlignmentStatistics stats) {

        Parser parser = new Parser();
        Stream<Alignment> stream = null;
        try {
            stream = file.getName().endsWith("sam") ? parser.parseSam(file) : parser.parseBam(file);
            stream.parallel().forEach(stats::update);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }



}
