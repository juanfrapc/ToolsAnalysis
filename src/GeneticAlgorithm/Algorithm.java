package GeneticAlgorithm;

import Application.AligningTask;
import Application.BWABackTrackTask;
import Application.BWAMEMTask;
import Application.BWASWTask;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.*;
import Model.Parameter;

import java.util.Arrays;

class Algorithm {

    private static final String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_1.fastq.gz";
    private static final String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_2.fastq.gz";
    private static final String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";

    private enum TaskTypes {
        MEM, SW ,ALN
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        int populationSize = 6;
        int selectionSize = 6;
        float mutationProbability = (float) 0.05;
        AligningTask task = taskSelection(args[0]);

        Fitness.clearMap();
        FreqFitness fitness = new FreqFitness(task, "FrequencyGeneticAlgorithm");
        Population population = new Population();
        Crossover crossover = new SPCrossover(fitness);
        Mutator mutator = new GaussianMutator(mutationProbability);
        PopulationMerger merger = new HallOfFameMerger();
        population.initilize(populationSize, fitness);

        System.out.println("Inicial Population.");
        System.out.println(population);

        boolean flag = true;
        int iteration = 0;
        while (flag || iteration < 20) {
            System.out.println("Iteration " + ++iteration + "... ... ...");
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
                mutator.mutate(ind, fitness);
            }
            Population merge = merger.merge(population, offspring, populationSize);
            if (population.equals(merge)) flag = false;
            else population = merge;

            System.out.println(population);
        }
        System.out.println("END");
    }

    private static AligningTask taskSelection(String arg) {
        switch (TaskTypes.valueOf(arg)) {
            case MEM:
                return new BWAMEMTask("MemFrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
            case SW:
                return new BWASWTask("SWFrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
            case ALN:
                return new BWABackTrackTask("FrequencyGeneticAlgorithm", forwardPath, reversePath, reference, new Parameter[0]);
        }
        return null;
    }
}
