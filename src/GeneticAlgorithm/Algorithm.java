package GeneticAlgorithm;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.*;

public class Algorithm {

    public static void main(String[] args) throws CloneNotSupportedException {
        int populationSize = 6;
        int selectionSize = 6;
        float mutationProbability = (float) 0.05;

        //FreqFitness fitness = new FreqFitness("GeneticAlgorithm");
        Population population = new Population();
        Crossover crossover = new SPCrossover(individual -> Float.parseFloat(individual.getParameters()[1].getValue()));
        Mutator mutator = new GaussianMutator(mutationProbability);
        PopulationMerger merger = new HallOfFameMerger();
        population.initilize(populationSize, individual -> Float.parseFloat(individual.getParameters()[1].getValue()));
        boolean flag = true;

        while(flag) {
            Population selected = Selection.roulette(population, selectionSize);
            Population offspring = new Population();
            for (int i = 0; i < selectionSize / 2; i++) {
                Individual father = selected.removeRandom();
                Individual mother = selected.removeRandom();
                Individual[] child = crossover.reproduce(father, mother);
                offspring.addIndividual(child[0]);
                offspring.addIndividual(child[1]);
            }
            for (Individual ind : offspring) {
                mutator.mutate(ind);
            }
            Population merge = merger.merge(population, offspring, populationSize);
            if (population.equals(merge)) flag = false;
            else population=merge;
        }


    }
}
