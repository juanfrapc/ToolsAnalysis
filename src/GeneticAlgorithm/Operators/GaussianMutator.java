package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.Parameter;

import java.util.Random;

public class GaussianMutator implements Mutator {

    private final float probability;

    public GaussianMutator(float probability) {
        this.probability = probability;
    }

    @Override
    public void mutate(Individual individual, Fitness fitness, char[] floats, char[] negatives) {
        Parameter[] parameters = individual.getParameters();
        Random random = new Random();
        boolean flag = false;
        for (int i = 1; i < parameters.length; i++) {
            if (random.nextFloat()<probability) {
                parameters[i].updateRandom(floats, negatives);
                System.out.println("*********** MUTADO " + parameters[i].getName() +" *********");
                flag=true;
            }
        }
        if(flag) individual.updateFitness(fitness);
    }
}
