package GeneticAlgorithm;

import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.*;

import java.util.Arrays;

class Algorithm {

    public static void main(String[] args) throws CloneNotSupportedException {
        int populationSize = 6;
        int selectionSize = 6;
        float mutationProbability = (float) 0.05;

        Fitness.clearMap();
        FreqFitness fitness = new FreqFitness("FrequencyGeneticAlgorithm");
        Population population = new Population();
        Crossover crossover = new SPCrossover(fitness);
        Mutator mutator = new GaussianMutator(mutationProbability);
        PopulationMerger merger = new HallOfFameMerger();
        population.initilize(populationSize, fitness);

        System.out.println("Inicial Population.");
        System.out.println(population);

        boolean flag = true;
        int iteration = 0;
        while(flag) {
            System.out.println("Iteration "+ ++iteration +"... ... ...");
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
            else population=merge;

            System.out.println(population);
        }
        System.out.println("END");
    }
}
