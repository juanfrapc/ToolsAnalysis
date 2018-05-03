package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class PicardTest {

    private final String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
    private final String fq1 = "Tests/tutorialFile/altalt.read1.fq";
    private final String fq2 = "Tests/tutorialFile/altalt.read2.fq";
    private final String interleaved = "Tests/tutorialFile/altalt_interleaved.fq";
    private final String ubam = "Tests/tutorialFile/altalt.ubam";
    private final String ubamMarked = "Tests/tutorialFile/altalt_markedIlluminaAdapters.bam";


    private final String sam = "Tests/tutorialFile/altalt.sam";
    private final String bam = "Tests/tutorialFile/altalt.bam";
    private final String sorted = "Tests/tutorialFile/altaltSorted.bam";
    private final String marked = "Tests/tutorialFile/altaltMarked.bam";
    private final String merged = "Tests/tutorialFile/altaltMerged.bam";
    private final String mergedIndex = "Tests/tutorialFile/altaltMerged.bai";

    private final String metrics = "Tests/tutorialFile/altaltmetrics.txt";
    private final String iluminaMetrics = "Tests/tutorialFile/altalt_iluminametrics.txt";

    @BeforeEach
    void setUp() {
        File interleaved = new File(this.interleaved);
        File ubam = new File(this.ubam);
        File ubamMarked = new File(this.ubamMarked);
        File bam = new File(this.bam);
        File sorted = new File(this.sorted);
        File marked = new File(this.marked);
        File merged = new File(this.merged);
        File mergedIndex = new File(this.mergedIndex);
        File metrics = new File(this.metrics);
        File metricsIll = new File(this.iluminaMetrics);
        if (interleaved.delete()) System.out.println("borrado");
        if (ubam.delete()) System.out.println("borrado");
        if (ubamMarked.delete()) System.out.println("borrado");
        if (bam.delete()) System.out.println("borrado");
        if (sorted.delete()) System.out.println("borrado");
        if (marked.delete()) System.out.println("borrado");
        if (metrics.delete()) System.out.println("borrado");
        if (merged.delete()) System.out.println("borrado");
        if (mergedIndex.delete()) System.out.println("borrado");
        if (metricsIll.delete()) System.out.println("borrado");

    }

    @Test
    void markDuplicates() throws IOException, InterruptedException {
        Process conerting = Samtools.sam2BamParallel(sam, bam);
        conerting.waitFor();
        Process sorting = Samtools.sortBamParallel(bam, sorted);
        sorting.waitFor();
        Process marking = Picard.markDuplicates(sorted, marked, metrics);
        int status = marking.waitFor();
        assert status == 0;
        assert new File(marked).exists();
        assert new File(metrics).exists();
    }

    @Test
    void fastQ2Sam() {
        try {
            Process fq2ubam = Picard.fastQ2Sam(fq1, fq2, ubam, "altalt", "altalt");
            int status = fq2ubam.waitFor();
            assert status == 0;
            assert new File(ubam).exists();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void markIlluminaAdapters() {
        try {
            Process fq2ubam = Picard.fastQ2Sam(fq1, fq2, ubam, "altalt", "altalt");
            fq2ubam.waitFor();
            Process mark = Picard.markIlluminaAdapters(ubam, ubamMarked, iluminaMetrics);
            int status = mark.waitFor();
            assert status == 0;
            assert new File(ubamMarked).exists();
            assert new File(iluminaMetrics).exists();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sam2fastq() {
        try {
            Process fq2ubam = Picard.fastQ2Sam(fq1, fq2, ubam, "altalt", "altalt");
            fq2ubam.waitFor();
            Process mark = Picard.markIlluminaAdapters(ubam, ubamMarked, iluminaMetrics);
            mark.waitFor();
            Process revert = Picard.sam2fastq(ubamMarked, interleaved);
            int status = revert.waitFor();
            assert status == 0;
            assert new File(interleaved).exists();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void mergeBamAlignment() {
        try {
            Process fq2ubam = Picard.fastQ2Sam(fq1, fq2, ubam, "altalt", "altalt");
            fq2ubam.waitFor();
            Process mark = Picard.markIlluminaAdapters(ubam, ubamMarked, iluminaMetrics);
            mark.waitFor();
            Process merge = Picard.mergeBamAlignment(ubamMarked, sam, merged, reference);
            int status = merge.waitFor();
            assert status == 0;
            assert new File(merged).exists();
            assert new File(mergedIndex).exists();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
