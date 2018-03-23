package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;

public interface Mutator {

    void mutate(Individual individual, Fitness fitness, char[] floats, char[] negatives);
}
