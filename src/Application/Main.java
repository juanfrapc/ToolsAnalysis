package Application;

import Model.AlignmentStatistics;
import Model.Parameter;
import Parser.ProcessFileStats;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String forwardPath = "EX51_HCF_01_S8_L001_R1_001.fastq.gz";
        String reversePath = "EX51_HCF_01_S8_L001_R2_001.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
        String outFile = ">> /media/uichuimi/DiscoInterno/Juanfra/BWAdefault.bam";
        String statsFile = ">> /media/uichuimi/DiscoInterno/Juanfra/stats.stat";
        AlignmentStatistics statistics = new AlignmentStatistics();

        Parameter[] parameters = new Parameter[]{
                new Parameter((char) 0, "mem"),
                new Parameter('M', ""),
                new Parameter('R', "@RG\\tPL:ILLUMINA\\tSM:DAM\\tID:C7BDUACXX.8\\tPU:C7BDUACXX.8.262\\tLB:TTAGGC"),
                new Parameter((char) 0, outFile)
        };

        BWAligner bwa = new BWAligner(forwardPath, reversePath, reference, parameters);
        System.out.println("Start running bwa");
        Process run = bwa.run();
        int error = run.waitFor();
        if (error == 0) System.out.println("BWA terminado con éxito");
        else System.out.println("Error de BWA: " + error);

        System.out.println("Obtaining stats");
        boolean process = ProcessFileStats.process(new File(outFile), statistics);
        if (process) System.out.println("Procesamiento terminado con éxito");
        else System.out.println("Error de procesado de estadisticas");

        FileStatsWriter writer = new FileStatsWriter(statsFile);
        writer.write(statistics);

    }

}
