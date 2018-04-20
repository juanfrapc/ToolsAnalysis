package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PicardTest {

    private final String sam = "Tests/ejemplo2.sam";
    private final String bam = "Tests/ejemplo2.bam";
    private final String sorted = "Tests/ejemplo2Sorted.bam";
    private final String marked= "Tests/ejemplo2Marked.bam";
    private final String metrics= "Tests/metrics.txt";

    @BeforeEach
    void setUp() {
        File bam = new File(this.bam);
        File sorted = new File(this.sorted);
        File marked= new File(this.marked);
        File metrics = new File(this.metrics);
        if (bam.delete()) System.out.println("borrado");
        if (sorted.delete()) System.out.println("borrado");
        if (marked.delete()) System.out.println("borrado");
        if (metrics.delete()) System.out.println("borrado");
    }

    @Test
    void markDuplicates() throws IOException, InterruptedException {
        Process conerting = Samtools.sam2BamParallel(sam, bam);
        conerting.waitFor();
        Process sorting = Samtools.sortBamParallel(bam, sorted);
        sorting.waitFor();
        Process marking = Picard.markDuplicates(sorted, marked, metrics);
        int status = marking.waitFor();
        assert status==0;
    }

}