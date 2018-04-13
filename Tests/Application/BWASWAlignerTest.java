package Application;

import Application.Aligners.BWASWAligner;
import Model.Aligner;
import Model.Parameter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ALL")
class BWASWAlignerTest {

    private String genome = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
    private String forward = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_forward.fastq.gz";
    private String reverse = "/media/uichuimi/Elements/GENOME_DATA/CONTROLS/DAM/FASTQ/DAM_reverse.fastq.gz";
    private String output = "/media/uichuimi/DiscoInterno/Juanfra/.test.sam";
    private String log = "/media/uichuimi/DiscoInterno/Juanfra/.logTest.log";
    private static Parameter[] parameters;
    private Aligner aligner;

    @BeforeAll
    public static void initParameters() {
        parameters = new Parameter[]{
                new Parameter('a', "5"),
                new Parameter('q', "3"),
        };
        //parameters = new Parameter[0];
    }

    @BeforeEach
    public void initAligner() {
        aligner = new BWASWAligner(forward, reverse, genome, output, log, parameters);
    }

    @Test
    public void runTest() {
        // Check if starts running
        try {
            Process process = aligner.runSimple();
            boolean status = process.waitFor(4, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso BWASW lanzado con errores", status);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("Excepcion lanzada");
        }
    }

}