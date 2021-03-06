package Control;

import Model.Alignment;
import Model.AlignmentsStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

public class Sam2StatsParser {

    public static boolean process(File file, AlignmentsStatistics stats) {

        Parser parser = new Parser();
        Stream<Alignment> stream;
        try {
            stream = file.getName().endsWith("sam") ? parser.parseSam(file) : parser.parseBam(file);
            stream.forEach(alignment -> {if (alignment != null) stats.update(alignment);});
        }catch (FileNotFoundException ex){
            System.out.println("Error FILENOTFOUND");
            return false;
        }
        catch (IOException e) {
            System.out.println("ERROR IO");
            return false;
        }
        return true;
    }



}
