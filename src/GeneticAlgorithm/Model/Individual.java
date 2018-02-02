package GeneticAlgorithm.Model;

import Model.Parameter;

import java.util.Arrays;
import java.util.Random;

public class Individual implements Comparable, Cloneable {

    private Parameter[] parameters;
    private float fitness;

    public Individual(Parameter[] parameters, Fitness fitness) {
        this.parameters = parameters;
        this.fitness = fitness.eval(this);
    }

    public static Individual getInitialRamdom(Fitness fitness) {
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
        for (int i = 1; i < values.length; i++) {
            double old = Double.parseDouble(values[i].getValue());
            double v = random.nextGaussian();
            double ne = old + v > 0 ? old + v : 0;
            values[i].setValue(ne +"");
        }
        return new Individual(values, fitness);
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public int compareTo(Object o) {
        Individual ind = (Individual) o;
        if (this.getFitness() - ind.getFitness() < 0) return -1;
        if (this.getFitness() - ind.getFitness() > 0) return 1;
        return 0;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public float getFitness() {
        return fitness;
    }

    @Override
    public Individual clone() throws CloneNotSupportedException {
        Parameter[] nParam = new Parameter[this.parameters.length];
        for (int i = 0; i < nParam.length; i++) {
            nParam[i] = parameters[i].clone();
        }
        Individual individual = new Individual(nParam, subject -> 0);
        individual.setFitness(this.fitness);
        return individual;
    }
}
