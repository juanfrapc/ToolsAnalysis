package GeneticAlgorithm;

import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.Parameter;

import java.util.Random;

public class SPCrossover implements Crossover{

    private Fitness fitness;

    SPCrossover(Fitness fitness) {
        this.fitness = fitness;
    }

    @Override
    public Individual[] reproduce(Individual parent1, Individual parent2) {
        Random random = new Random();
        Parameter[] parametersParent1 = parent1.getParameters();
        int length = parametersParent1.length;
        Parameter[] parametersParent2 = parent2.getParameters();
        int crossPoint = random.nextInt(length) + 1;

        Parameter[] child1 = new Parameter[length];
        System.arraycopy(parametersParent1, 0, child1, 0, crossPoint);
        System.arraycopy(parametersParent2, crossPoint, child1, crossPoint, length - crossPoint);

        Parameter[] child2 = new Parameter[length];
        System.arraycopy(parametersParent1, crossPoint, child2, crossPoint, length -crossPoint);
        System.arraycopy(parametersParent2, 0, child2, 0, crossPoint);

        return new Individual[]{ new Individual(child1, fitness), new Individual(child2, fitness)};
    }

}
