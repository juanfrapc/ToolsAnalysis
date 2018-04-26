package Application;

import Application.Aligners.BWAMEMAligner;
import Model.Aligner;
import Model.Parameter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

@SuppressWarnings("ALL")
public class BWAMEMAlignerTest {

    private String genome = "/home/juanfrapc/GENOME_DATA/REFERENCE/genome.fasta";
    private String forward = "/home/juanfrapc/GENOME_DATA/DAM/FASTQ/DAM_forward.fastq.gz";
    private String reverse = "/home/juanfrapc/GENOME_DATA/DAM/FASTQ/DAM_reverse.fastq.gz";
    private String output = "/home/juanfrapc/GENOME_DATA/DAM/.test.sam";
    private String log = "/home/juanfrapc/GENOME_DATA/DAM/.logTest.log";
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

    @Test
    public void otherTest(){
        parameters = new Parameter[]{
                new Parameter('k',"19"),
                new Parameter('w',"101"),
                new Parameter('d',"100"),
                new Parameter('r',"0.4020100172835497"),
                new Parameter('c',"9998"),
                new Parameter('A',"1"),
                new Parameter('B',"3"),
                new Parameter('O',"5"),
                new Parameter('E',"0"),
                new Parameter('L',"4"),
                new Parameter('U',"9"),
        };
        aligner = new BWAMEMAligner(forward, reverse, genome, output, log, parameters);
        try {
            Process process = aligner.run();
            boolean status = process.waitFor(20, TimeUnit.SECONDS);
            process.destroyForcibly();
            assertFalse("Proceso lanzado con errores", status);
        } catch (Exception e) {
            assert (false);
        }
    }
}