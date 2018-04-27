package Control;

import Model.Alignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SamtoolsTest {

    private final String sam = "Tests/tutorialFile/altalt.sam";
    private final String bam = "Tests/tutorialFile/altalt.bam";
    private final String sorted = "Tests/tutorialFile/altaltSorted.bam";
    private final String index= "Tests/tutorialFile/altaltSorted.bam.bai";

    @BeforeEach
    void setUp() {
        File bam = new File(this.bam);
        File sorted = new File(this.sorted);
        File index = new File(this.index);
        if (bam.delete()) System.out.println("borrado");
        if (sorted.delete()) System.out.println("borrado");
        if (index.delete()) System.out.println("borrado");
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
    void convert() {
        try {
            Process process = Samtools.sam2Bam(sam, bam);
            int status = process.waitFor();
            assert(status == 0);
            checkResult();
        } catch (Exception e) {
            assert (false);
        }
    }


    @Test
    void convertParallel() {
        try {
            Process process = Samtools.sam2BamParallel(sam, bam);
            process.waitFor();
            checkResult();
        } catch (Exception e) {
            assert (false);
        }
    }

    private void checkSorted(String file) throws IOException {
        final int[] pos = {0};
        final String[] posChr= {""};
        Stream<Alignment> bamStream = new Parser().parseBam(new File(file));
        bamStream.forEach(alignment -> {
            int alnPos = Integer.parseInt(alignment.getParam(Alignment.POS));
            String alnChr = alignment.getParam(Alignment.RNAME);
            assert(alnPos >= pos[0] || !alnChr.equals(posChr[0]));
            pos[0] = alnPos;
            posChr[0] = alnChr;});
    }

    private void checkUnSorted(String file) throws IOException {
        final int[] pos = {0};
        final boolean[] unsorted = {false};
        Stream<Alignment> bamStream = new Parser().parseBam(new File(file));
        bamStream.forEach(alignment -> {
            int alnPos = Integer.parseInt(alignment.getParam(Alignment.POS));
            if (!unsorted[0])unsorted[0] = pos[0] > alnPos;
            pos[0] = alnPos;});
        assert(unsorted[0]);
    }

    @Test
    void sortBam() throws IOException, InterruptedException {
        Process process = Samtools.sam2Bam(sam, bam);
        process.waitFor();
        Process sorting = Samtools.sortBam(bam, sorted.split("\\.")[0]);
        sorting.waitFor();
        checkSorted(sorted);
        checkUnSorted(bam);
    }

    @Test
    void sortBamParallel() throws IOException, InterruptedException {
        Process process = Samtools.sam2BamParallel(sam, bam);
        process.waitFor();
        Process sorting = Samtools.sortBamParallel(bam, sorted);
        sorting.waitFor();
        checkSorted(sorted);
        checkUnSorted(bam);
    }

    @Test
    void index() throws IOException, InterruptedException {
        Process process = Samtools.sam2BamParallel(sam, bam);
        process.waitFor();
        Process sorting = Samtools.sortBamParallel(bam, sorted);
        sorting.waitFor();
        Process indexing = Samtools.index(sorted);
        int status = indexing.waitFor();
        assert status ==0;
        assert new File(index).exists();
    }
}
