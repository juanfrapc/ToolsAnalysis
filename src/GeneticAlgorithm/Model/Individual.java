package GeneticAlgorithm.Model;

import Model.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class Individual implements Comparable<Individual>, Cloneable {

    private final Parameter[] parameters;
    private float fitness;

    public Individual(Parameter[] parameters, Fitness fitness) {
        this.parameters = parameters;
        this.fitness = fitness.eval(this);
    }

    public Individual(Parameter[] param, float fitness) {
        this.parameters = param;
        this.fitness = fitness;
    }

    @NotNull
    public static Individual getInitialRamdom(Fitness fitness) {
        Parameter[] values = new Parameter[]{
                new Parameter('k', "19"),
                new Parameter('w', "100"),
                new Parameter('d', "100"),
                new Parameter('r', "1.5"),
                new Parameter('c', "500"),
                new Parameter('A', "1"),
                new Parameter('B', "4"),
                new Parameter('O', "6"),
                new Parameter('E', "1"),
                new Parameter('L', "5"),
                new Parameter('U', "17"),
        };
        Random random = new Random();
        for (Parameter value : values) {
            value.updateRandom();
        }
        return new Individual(values, fitness);
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public int compareTo(@NotNull Individual ind) {
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
        super.clone();
        Parameter[] nParam = new Parameter[this.parameters.length];
        for (int i = 0; i < nParam.length; i++) {
            nParam[i] = parameters[i].clone();
        }
        return new Individual(nParam, getFitness());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;

        Individual that = (Individual) o;

        return Float.compare(that.getFitness(), getFitness()) == 0 && Arrays.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getParameters());
    }


    public void updateFitness(Fitness fitness) {
        this.fitness = fitness.eval(this);
    }
}
