package GeneticAlgorithm;

import Model.Parameter;

import java.util.Random;

public class Individual implements Comparable, Cloneable {

    private Parameter[] parameters;
    private float fitness;

    public Individual(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public static Individual getInitialRamdom(String name) {
        Parameter[] values = new Parameter[]{
                new Parameter("mem"),
                new Parameter('k', "19"),
                new Parameter('w', "100"),
                new Parameter('d', "100"),
                new Parameter('r', "1.5"),
                new Parameter('c', "10000"),
                new Parameter('A', "1"),
                new Parameter('B', "4"),
                new Parameter('O', "6"),
                new Parameter('E', "1"),
                new Parameter('L', "5"),
                new Parameter('U', "9"),
        };
        Random random = new Random();
        for (int i = 0; i < values.length; i++) values[i].setValue(values[i].getValue() + random.nextGaussian());
        Individual individual = new Individual(values);
        individual.setFitness(Fitness.eval(name, individual));
        return individual;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public int compareTo(Object o) {
        Individual ind = (Individual) o;
        if (this.getFitness() - ind.getFitness() < 0) return -1;
        if (this.getFitness() - ind.getFitness() < 0) return 1;
        return 0;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getFitness() {
        return fitness;
    }

    @Override
    protected Individual clone() throws CloneNotSupportedException {
        Individual individual = new Individual(this.parameters);
        individual.setFitness(this.fitness);
        return individual;
    }
}
