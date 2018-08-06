package Control;

import Model.Variant;
import Model.VariantStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

public class VCF2StatsParser {

    public static boolean process(File file, VariantStatistics stats, boolean woman) {

        Parser parser = new Parser();
        Stream<Variant> stream;
        try {
            stream = parser.parseVCF(file);
            stream.forEach(variant-> {if (variant!= null) stats.update(variant, woman);});
        }catch (FileNotFoundException ex){
            System.out.println("Error FILENOTFOUND");
            return false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

}
