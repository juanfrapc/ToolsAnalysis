package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Individual;
import Model.Parameter;

import java.util.Random;

public class GaussianMutator implements Mutator {

    private final float probability;

    public GaussianMutator(float probability) {
        this.probability = probability;
    }

    @Override
    public void mutate(Individual individual) {
        Parameter[] parameters = individual.getParameters();
        Random random = new Random();
        for (int i = 1; i < parameters.length; i++) {
            if (random.nextFloat()<probability) {
                double old = Double.parseDouble(parameters[i].getValue());
                double v = random.nextGaussian();
                double ne = old + v > 0 ? old + v : 0;
                parameters[i].setValue(ne + "");
            }
        }
    }
}
