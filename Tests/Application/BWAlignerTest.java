package Application;

import Model.Aligner;
import Model.Parameter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

public class BWAlignerTest {

    private String genome = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
    private String forward = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_forward.fastq.gz";
    private String reverse = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_reverse.fastq.gz";
    private String output = "/media/uichuimi/DiscoInterno/Juanfra/.test.bam";
    private static Parameter[] parameters;
    private Aligner aligner;

    @BeforeClass
    public static void initParameters() {
        parameters = new Parameter[]{
                new Parameter((char) 0, "mem"),
                new Parameter('w', "20"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC")
        };
    }

    @Before
    public void initAligner() {
        aligner = new BWAligner(forward, reverse, genome, output, parameters);
    }

    @Test
    public void runTest() {
        // Check if starts running
        try {
            Process process = aligner.run();
            boolean status = process.waitFor(4, TimeUnit.SECONDS);
            assertFalse("Proceso lanzado con errores", status);
        } catch (Exception e) {
            assert (false);
        }
    }
}