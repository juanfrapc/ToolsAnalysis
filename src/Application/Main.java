package Application;

import Model.Parameter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String name= "default";
        String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R1_001.fastq.gz";
        String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R2_001.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";

        Parameter[] parameters = new Parameter[]{
                new Parameter((char) 0, "mem"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC"),
        };

        BWATask task = new BWATask(name, forwardPath, reversePath, reference, parameters);
        task.run();

    }

}
