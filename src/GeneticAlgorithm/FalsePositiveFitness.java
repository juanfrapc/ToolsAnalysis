package GeneticAlgorithm;

import Control.GermlineSNP;
import Control.PreProcessor;
import Control.VCF2StatsParser;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.VariantStatistics;

import java.io.File;
import java.io.IOException;

public class FalsePositiveFitness extends Fitness {


    private String ubam;
    private String fullFastq;
    private String reference;
    private String name;
    private String pathFull;
    private String alignerType;
    private VariantStatistics variantStatistics;

    public FalsePositiveFitness(String reference, String name, String pathFull, String alignerType) {
        this.ubam = pathFull + "FASTQ/" + name + ".ubam";
        this.fullFastq = pathFull + "FASTQ/" + name + "_interleaved.fq.gz";
        this.reference = reference;
        this.name = name;
        this.pathFull = pathFull;
        this.alignerType = alignerType;
        variantStatistics = new VariantStatistics();
    }

    @Override
    public float eval(Individual individual) {
        float falsePositive;
        if (Fitness.containsVCF(individual)) {
            falsePositive = Fitness.getVCFFitness(individual);
        } else {
            try {
                PreProcessor.getPreprocessedFromInterleavedParm(ubam, fullFastq, reference, name + "_GEN",
                        pathFull + "BAM/", individual.getParameters(), alignerType);
                GermlineSNP.getVCFilteredFromSingelBAM(reference, name + "_GEN", pathFull + "BAM/", pathFull + "VCF/");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (new File(pathFull + "VCF/" + name + "_GEN_FullRecal.vcf").exists()) {
                VCF2StatsParser.process(new File(pathFull + "VCF/" + name + "_GEN_FullRecal.vcf"),
                        variantStatistics, true);
            } else if (new File(pathFull + "VCF/" + name + "_GEN_SNPRecal.vcf").exists()) {
                VCF2StatsParser.process(new File(pathFull + "VCF/" + name + "_GEN_SNPRecal.vcf"),
                        variantStatistics, true);
            } else {
                VCF2StatsParser.process(new File(pathFull + "VCF/" + name + "_GEN_raw.vcf"),
                        variantStatistics, true);
            }
            falsePositive = ((float) variantStatistics.getFalsePositive()) / ((float) variantStatistics.getTotal());
            Fitness.putVCF(individual, falsePositive);
        }
        return falsePositive;
    }

    public long getFalsePositive(){
        return variantStatistics.getFalsePositive();
    }
}
