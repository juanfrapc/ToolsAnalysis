package Control;

import Model.Alignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Sam2BamTest {

    private final String sam = "Tests/ejemplo2.sam";
    private final String bam = "Tests/ejemplo2.bam";

    @BeforeEach
    void setUp() {
        File file = new File(bam);
        if (file.delete()) System.out.println("borrado");

    }

    @Test
    void convert() {
        try {
            Process process = Samtools.convert(sam, bam);
            process.waitFor();
            checkResult();
        } catch (Exception e) {
            assert (false);
        }
    }

    private void checkResult() throws IOException {
        Stream<Alignment> samStream = new Parser().parseSam(new File(sam)).filter(alignment -> alignment != null);
        Stream<Alignment> bamStream = new Parser().parseBam(new File(bam));
        Iterator<Alignment> iter1 = samStream.iterator(), iter2 = bamStream.iterator();
        while (iter1.hasNext() && iter2.hasNext())
            assertEquals(iter1.next(), iter2.next());
        assert !iter1.hasNext() && !iter2.hasNext();
    }

    @Test
    void convertParallel() {
        try {
            Process process = Samtools.convertParallel(sam, bam);
            process.waitFor();
            checkResult();
        } catch (Exception e) {
            assert (false);
        }
    }

}
