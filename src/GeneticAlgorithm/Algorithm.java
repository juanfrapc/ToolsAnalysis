package GeneticAlgorithm;

import Application.AlignningStatsTasks.AligningTask;
import Application.AlignningStatsTasks.BWABackTrackTask;
import Application.AlignningStatsTasks.BWAMEMTask;
import Application.AlignningStatsTasks.BWASWTask;
import Control.VCF2StatsParser;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.*;
import Model.Parameter;
import Model.VariantStatistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Algorithm {


    private enum TaskTypes {
        MEM, SW, ALN
    }

    public static BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter(new File("/home/juanfrapc/GENOME_DATA/stats/stats.txt"), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws CloneNotSupportedException, IOException, InterruptedException {
        String name = "DAM";
        String pathSimplified = "/media/uichuimi/DiscoInterno/GENOME_DATA/DAM_SIMPLIFIED/";
        String pathFull = "/media/uichuimi/DiscoInterno/GENOME_DATA/DAM/";
        String originalVCF = pathFull + "VCF/DAM_20180730_GATK_GRCH38_FullRecal.vcf";
        String reference = "/media/uichuimi/DiscoInterno/GENOME_DATA/REFERENCE/gatk_resourcebunde_GRCH38.fasta";
        boolean woman = true;
        if (args.length > 1) {
            name = args[1].split("=")[1].trim();
            pathSimplified = args[2].split("=")[1].trim();
            pathFull = args[3].split("=")[1].trim();
            originalVCF = args[4].split("=")[1].trim();
            woman = args[5].split("=")[1].trim().contains("woman");
        }

        String alignerType = args.length > 0 ? args[0] : "MEM";
        applyEvolution(alignerType, reference, pathSimplified, pathFull, originalVCF, name, woman);
    }

    static void applyEvolution(String alignerType, String reference, String pathSimplified, String pathFull, String originalVCF, String name, boolean woman) throws CloneNotSupportedException, IOException, InterruptedException {
        String interleavedPath = pathSimplified + "FASTQ/simplified_interleaved.fq.gz";
        String forwardPath = alignerType.equals("MEM") ? interleavedPath : pathSimplified + "FASTQ/simplified_forward.fastq.gz";
        String reversePath = pathSimplified + "FASTQ/simplified_reverse.fastq.gz";

        VariantStatistics variantStatistics = new VariantStatistics();
        VCF2StatsParser.process(new File(originalVCF),
                variantStatistics, woman);
        System.out.println(variantStatistics.getTotal());
        float falsePositive = ((float) variantStatistics.getFalsePositive()) / ((float) variantStatistics.getTotal());
        writer.append("##### " + variantStatistics.getFalsePositive() + "(" + falsePositive + ")" + "-> default\n");
        System.out.println("##### " + variantStatistics.getFalsePositive() + "(" + falsePositive + ")" + "-> default");

        int populationSize = 16;
        int selectionSize = 10;
        float mutationProbability = (float) 0.1;
        AligningTask task = taskSelection(alignerType, forwardPath, reversePath, reference);

        Fitness.clearMap();
        FreqFitness fitness = new FreqFitness(task, alignerType + "FrequencyGeneticAlgorithm");
        FalsePositiveFitness falsePositiveFitness = new FalsePositiveFitness(reference, name, pathFull, alignerType);
        Population population = new Population();
        Crossover crossover = new SPCrossover(fitness);
        Mutator mutator = new GaussianMutator(mutationProbability);
        PopulationMerger merger = new HallOfFameMerger();
        population.initilize(populationSize, fitness, getInitialValues(alignerType), getFloats(alignerType), getNegatives(alignerType));

        System.out.println("Inicial Population.");
        System.out.println(population);
        writer.append("New try " + alignerType + "\n");

        int iteration = 0;
        int unimproved = 0;
        while (unimproved < 5 && iteration < 100 || iteration < 15) {
            System.out.println("Iteration " + ++iteration + " ... ... ...");
            Population selected = Selection.roulette(population, selectionSize);
            Population offspring = new Population();
            for (int i = 0; i < selectionSize / 2; i++) {
                Individual father = selected.removeRandom();
                System.out.println("Padre: " + Arrays.toString(father.getParameters()));
                Individual mother = selected.removeRandom();
                System.out.println("Madre: " + Arrays.toString(mother.getParameters()));
                Individual[] child = crossover.reproduce(father, mother);
                System.out.println("Hijos: " + Arrays.toString(child[0].getParameters()) + " y " + Arrays.toString(child[1].getParameters()));
                System.out.println("----------------------------------------------------------");
                offspring.addIndividual(child[0]);
                offspring.addIndividual(child[1]);
            }
            for (Individual ind : offspring) {
                mutator.mutate(ind, fitness, getFloats(alignerType), getNegatives(alignerType));
            }
            Population merge = merger.merge(population, offspring, populationSize);
            if (population.equals(merge)) unimproved++;
            else {
                unimproved = 0;
                population = merge;
            }
            Individual best = population.getBest();
            writer.append(best.getFitness() + "->" + Arrays.toString(best.getParameters()) + "\n");
            System.out.println("best:" + best.getFitness() + "->" + Arrays.toString(best.getParameters()) + "\n");
            if (iteration % 10 == 0) {
                falsePositive = falsePositiveFitness.eval(best);
                writer.append("##### " + variantStatistics.getFalsePositive() + "(" + falsePositive + ")" + "->" + Arrays.toString(best.getParameters()) + "\n");
                System.out.println("##### " + variantStatistics.getFalsePositive() + "(" + falsePositive + "->" + Arrays.toString(best.getParameters()) + "\n");
            }
        }
        System.out.println("END");
        writer.close();
    }


    private static AligningTask taskSelection(String aligner, String forward, String reverse, String reference) {
        switch (TaskTypes.valueOf(aligner)) {
            case MEM:
                return new BWAMEMTask("MemFrequencyGeneticAlgorithm", forward, "", reference, new Parameter[0]);
            case SW:
                return new BWASWTask("SWFrequencyGeneticAlgorithm", forward, reverse, reference, new Parameter[0]);
            case ALN:
                return new BWABackTrackTask("ALNFrequencyGeneticAlgorithm", forward, reverse, reference, new Parameter[0]);
        }
        return null;
    }

    public static Parameter[] getInitialValues(String aligner) {
        switch (TaskTypes.valueOf(aligner)) {
            case MEM:
                return new Parameter[]{
                        new Parameter('k', "19"),
                        new Parameter('w', "100"),
                        new Parameter('d', "100"),
                        new Parameter('r', "1.5"),
                        new Parameter('c', "500"),
                        new Parameter('A', "1"),
                        new Parameter('B', "4"),
                        new Parameter('O', "6"),
                        new Parameter('E', "1"),
                        new Parameter('L', "5"),
                        new Parameter('U', "17"),
                };
            case SW:
                return new Parameter[]{
                        new Parameter('a', "1"),
                        new Parameter('b', "3"),
                        new Parameter('q', "5"),
                        new Parameter('r', "2"),
                        new Parameter('w', "33"),
                        new Parameter('T', "37"),
                        new Parameter('c', "5.5"),
                        new Parameter('z', "1"),
                        new Parameter('s', "3"),
                        new Parameter('N', "5"),
                };
            case ALN:
                return new Parameter[]{
                        new Parameter('n', "0.04"),
                        new Parameter('o', "1"),
                        new Parameter('e', "-1"),
                        new Parameter('d', "16"),
                        new Parameter('i', "5"),
                        new Parameter('k', "2"),
                        new Parameter('M', "3"),
                        new Parameter('O', "11"),
                        new Parameter('E', "4"),
                };
            default:
                System.out.println("Error getting initial values");
                return new Parameter[0];
        }
    }

    public static char[] getFloats(String aligner) {
        switch (TaskTypes.valueOf(aligner)) {
            case MEM:
                return new char[]{'r'};
            case SW:
                return new char[]{'c'};
            case ALN:
                return new char[]{'n'};
        }
        return null;
    }

    public static char[] getNegatives(String aligner) {
        switch (TaskTypes.valueOf(aligner)) {
            case MEM:
                return new char[]{};
            case SW:
                return new char[]{};
            case ALN:
                return new char[]{'e'};
        }
        return null;
    }
}
