package GeneticAlgorithm;

import Application.BWAMEMTask;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.AlignmentsStatistics;
import Parser.ParseFileStats;

import java.io.File;

public class FreqFitness implements Fitness{

    private static final String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R1_001.fastq.gz";
    private static final String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/HCF/HCF001/EX51_HCF_01_S8_L001_R2_001.fastq.gz";
    private static final String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
    private String name;

    FreqFitness(String name) {
        this.name = name;
    }

    public float eval(Individual individual){

        BWAMEMTask task = new BWAMEMTask(name, forwardPath, reversePath, reference, individual.getParameters());
        task.run();
        AlignmentsStatistics stats = new AlignmentsStatistics();
        ParseFileStats.process(new File("/media/uichuimi/DiscoInterno/Juanfra/" + name + ".stat"), stats);
        return stats.getUniquelyMapped()/(float) stats.getTotal();
    }

}
