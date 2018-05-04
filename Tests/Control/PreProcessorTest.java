package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PreProcessorTest {

    private final String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
    private final String fq1 = "Tests/tutorialFile/altalt.read1.fq";
    private final String fq2 = "Tests/tutorialFile/altalt.read2.fq";
    private final String ubam = "Tests/tutorialFile/altalt.ubam";
    private final String interleaved = "Tests/tutorialFile/altalt_interleaved.fq.gz";

    private final String path = "/home/juanfrapc/IdeaProjects/ToolsAnalysis/Tests/tutorialFile/";

    @BeforeEach
    void setUp() {
        File interleaved = new File(this.path + "altalt_interleaved.fq");
        File interleavedGz = new File(this.path + "altalt_interleaved.fq.gz");
        File finalFile = new File(this.path + "Final.bam");
        if (interleaved.delete()) System.out.println("borrado");
        if (interleavedGz.delete()) System.out.println("borrado");
        if (finalFile.delete()) System.out.println("borrado");
    }

    @Test
    void getInterleavedFastq() {
        try {
            boolean status = PreProcessor.getInterleavedFastq(fq1, fq2, "altalt", path, "altalt");
            assert status;
            assert new File(this.path + "altalt_interleaved.fq.gz").exists();
            assert new File(this.path + "altalt_interleaved.fq.gz").exists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPreprocessedFromInterleaved() {
        try {
            PreProcessor.getInterleavedFastq(fq1, fq2, "altalt", path, "altalt");
            boolean status = PreProcessor.getPreprocessedFromInterleaved(ubam, interleaved,reference, "altalt", path);
            assert status;
            assert new File(this.path + "altaltFinal.bam").exists();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPreprocessedFromPaired() {
    }

}