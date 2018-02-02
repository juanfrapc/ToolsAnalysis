package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Population;

public interface PopulationMerger {

    Population merge(Population pop1, Population pop2, int size);

}
