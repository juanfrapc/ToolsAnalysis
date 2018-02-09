package Application;

import Application.Aligners.BWABackTrackAlnAligner;
import Application.Aligners.BWABackTrackSampe;
import Model.Aligner;
import Model.Parameter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

@SuppressWarnings("ALL")
public class BWABackTrackTest {

    private String genome = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
    private String forward = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_forward.fastq.gz";
    private String reverse = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_reverse.fastq.gz";
    private String output = "/media/uichuimi/DiscoInterno/Juanfra/.test.sam";
    private String log = "/media/uichuimi/DiscoInterno/Juanfra/.logTest.log";
    private static Parameter[] parameters;
    private Aligner forwardAligner;
    private Aligner reverseAligner;
    private Aligner sampeAligner;

    @BeforeClass
    public static void initParameters() {
        parameters = new Parameter[]{
                new Parameter('n', "20"),
                new Parameter('0', ""),
        };
    }

    @Before
    public void initAligner() {
        forwardAligner = new BWABackTrackAlnAligner(forward, genome, output, log, parameters);
        reverseAligner = new BWABackTrackAlnAligner(forward, genome, output, log, parameters);
        sampeAligner = new BWABackTrackSampe("/media/uichuimi/Elements/Copia20160125/ExomeSuite/temp/S072_seq1.sai",forward,
                "/media/uichuimi/Elements/Copia20160125/ExomeSuite/temp/S072_seq1.sai", reverse, genome, output, log);
    }

    @Test
    public void runTestALN() {
        // Check if starts running
        try {
            Process process = forwardAligner.run();
            boolean status = process.waitFor(4, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso forward lanzado con errores", status);
            process = reverseAligner.run();
            status = process.waitFor(4, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso reverse lanzado con errores", status);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void runTestSampe() {
        // Check if starts running
        try {
            Process process = sampeAligner.run();
            boolean status = process.waitFor(2, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso Sampe lanzado con errores", status);
        } catch (Exception e) {
            assert (false);
        }
    }
}