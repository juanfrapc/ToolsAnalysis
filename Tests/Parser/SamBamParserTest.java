package Parser;

import Model.Alignment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class SamBamParserTest {

    private final File samFile = new File("ejemplo.sam");
    private final File bamFile = new File("/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss.bam");
    private Stream<Alignment> samStream;
    private Stream<Alignment> bamStream;

    @BeforeClass
    public void init() {
        try {
            samStream = new Parser().parseSam(samFile);
            bamStream = new Parser().parseBam(bamFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkInteger(String str) {
        try {
            int i = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean checkParam(Alignment alignment) {
        return (alignment.getSize() == 11) &&
                (checkInteger(alignment.getParam(Alignment.FLAG))) &&
                (checkInteger(alignment.getParam(Alignment.MAPQ))) &&
                (checkInteger(alignment.getParam(Alignment.PNEXT))) &&
                (checkInteger(alignment.getParam(Alignment.POS))) &&
                (checkInteger(alignment.getParam(Alignment.TLEN)));
    }

    @Test
    public void samTestFIleShow() throws IOException {
        samStream.parallel().forEach(System.out::println);
    }

    @Test
    public void samParamTest() {
        samStream.parallel().forEach(alignment -> {
            assert (checkParam(alignment));
        });
    }

    @Test
    public void bamTestFileShow() {
        bamStream.parallel().limit(5).forEach(System.out::println);
    }

    @Test
    public void bamParamTest() {
        bamStream.limit(50).parallel().forEach(alignment -> {
            assert (checkParam(alignment));
        });
    }

}
