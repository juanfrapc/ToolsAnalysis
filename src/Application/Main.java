package Application;

import Model.Parameter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R1_001.fastq.gz";
        String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R2_001.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";

        String name = "default";
        Parameter[] parameters = new Parameter[]{
                new Parameter("mem"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC"),
        };
        BWAMEMTask task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.start();

        name = "mem1";
        parameters = new Parameter[]{
                new Parameter("mem"),
                new Parameter('r', "1"),
                new Parameter('w', "100"),
                new Parameter('A', "10"),
                new Parameter('O', "4"),
                new Parameter('E', "1"),
        };
        task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.start();

        name = "mem2";
        parameters = new Parameter[]{
                new Parameter("mem"),
                new Parameter('r', "1"),
                new Parameter('w', "50"),
                new Parameter('A', "6"),
                new Parameter('O', "4"),
                new Parameter('E', "2"),
        };
        task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.start();

    }

}
