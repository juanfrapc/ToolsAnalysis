package Application;

import Model.Aligner;
import Model.Parameter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class BWAlignerTest {

    private File genome = new File("/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa");
    private File forward = new File("/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R1_001.fastq.gz");
    private File reverse = new File("/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R2_001.fastq.gz");
    private static Parameter[] parameters;
    private Aligner aligner;

    @BeforeClass
    public static void initParameters(){
        parameters = new Parameter[]{
                new Parameter((char) 0, "mem"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC"),
        };
    }

    @Before
    public void initAligner(){
        aligner = new BWAligner(forward, reverse, genome, parameters);
    }

    @Test
    public void runTest() {
        // Check if starts running
        try {
            Process process = aligner.run();
            process.destroyForcibly();
            assert(true);
        } catch (IOException e) {
            assert(false);
        }
    }
}