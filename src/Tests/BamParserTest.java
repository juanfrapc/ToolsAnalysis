package Tests;

import Model.Alignment;
import org.junit.Test;
import parse.BamParser;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class BamParserTest {

    final File file = new File("/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss.bam");
    final Stream<Alignment> stream = new BamParser().parseBam(file);

    public BamParserTest() throws IOException {
    }

    @Test
    public void test() {
        stream.parallel().forEach(alignment -> {
            System.out.println(alignment);
        });
    }
}