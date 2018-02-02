package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Individual;

public interface Crossover {

    Individual[] reproduce(Individual parent1, Individual parent2);

    default String id() {
        return "crossover";
    }

}
