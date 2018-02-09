package Application;

import Application.Aligners.BWAMEMAligner;
import Model.Aligner;
import Model.Parameter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

class BWAMEMAlignerTest {

    private static Parameter[] parameters;
    private Aligner aligner;

    @BeforeClass
    public static void initParameters() {
        parameters = new Parameter[]{
                new Parameter('w', "20"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC")
        };
    }

    @Before
    public void initAligner() {
        String log = "/media/uichuimi/DiscoInterno/Juanfra/.logTest.log";
        String output = "/media/uichuimi/DiscoInterno/Juanfra/.test.sam";
        String reverse = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_reverse.fastq.gz";
        String forward = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_forward.fastq.gz";
        String genome = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
        aligner = new BWAMEMAligner(forward, reverse, genome, output, log, parameters);
    }

    @Test
    public void runTest() {
        // Check if starts running
        try {
            Process process = aligner.run();
            boolean status = process.waitFor(4, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso lanzado con errores", status);
        } catch (Exception e) {
            assert (false);
        }
    }
}