package GeneticAlgorithm;

import Application.AlignningStatsTasks.AligningTask;
import Application.AlignningStatsTasks.BWABackTrackTask;
import Application.AlignningStatsTasks.BWAMEMTask;
import Application.AlignningStatsTasks.BWASWTask;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.*;
import Model.Parameter;

import java.util.Arrays;

public class Algorithm {

    private static final String forwardPath = "/home/juanfrapc/GENOME_DATA/DAM/FASTQ/DAM_forward.fastq.gz";
    private static final String reversePath = "/home/juanfrapc/GENOME_DATA/DAM/FASTQ/DAM_reverse.fastq.gz";
    private static final String reference = "/home/juanfrapc/GENOME_DATA/REFERENCE/genome.fasta";

    private enum TaskTypes {
        MEM, SW, ALN
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        int populationSize = 6;
        int selectionSize = 6;
        float mutationProbability = (float) 0.05;
        String alignerType = args.length > 0 ? args[0] : "MEM";
        AligningTask task = taskSelection(alignerType);

        Fitness.clearMap();
        FreqFitness fitness = new FreqFitness(task, alignerType + "FrequencyGeneticAlgorithm");
        Population population = new Population();
        Crossover crossover = new SPCrossover(fitness);
        Mutator mutator = new GaussianMutator(mutationProbability);
        PopulationMerger merger = new HallOfFameMerger();
        population.initilize(populationSize, fitness, getInitialValues(alignerType), getFloats(alignerType), getNegatives(alignerType));

        System.out.println("Inicial Population.");
        System.out.println(population);

        boolean flag = true;
        int iteration = 0;
        while (flag || iteration < 15) {
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
                offspring.addIndividual(child[0]);
                offspring.addIndividual(child[1]);
            }
            for (Individual ind : offspring) {
                mutator.mutate(ind, fitness, getFloats(alignerType), getNegatives(alignerType));
            }
            Population merge = merger.merge(population, offspring, populationSize);
            if (population.equals(merge)) flag = false;
            else population = merge;

            System.out.println(population);
        }
        System.out.println("END");
    }



    private static AligningTask taskSelection(String aligner) {
        switch (TaskTypes.valueOf(aligner)) {
            case MEM:
                return new BWAMEMTask("MemFrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
            case SW:
                return new BWASWTask("SWFrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
            case ALN:
                return new BWABackTrackTask("ALNFrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
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
        }
        return null;
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
