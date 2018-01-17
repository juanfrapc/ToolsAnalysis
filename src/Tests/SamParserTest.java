package Tests;

import Model.Alignment;
import org.junit.Test;
import parse.BamParser;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class SamParserTest {

    @Test
    public void test() {
        final File file = new File("../ejemplo.sam");
        final Stream<Alignment> stream;
        try {
            stream = new BamParser().parseSam(file);
            stream.parallel().forEach(alignment -> {
                System.out.println(alignment);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
