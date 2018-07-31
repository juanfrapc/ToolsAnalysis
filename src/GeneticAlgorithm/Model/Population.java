package GeneticAlgorithm.Model;


import Model.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class Population implements Iterable<Individual> {

    private final ArrayList<Individual> list;

    public Population() {
        list = new ArrayList<>();
    }

    public void addIndividual(Individual ind) {
        list.add(ind);
    }

    public void initilize(int size, Fitness fitness, Parameter[] initialValues, char[] floats, char[] negatives) {
        for (int i = 0; i < size; i++) {
            Parameter[] initialValuesCloned = new Parameter[initialValues.length];
            for (int j = 0; j < initialValues.length; j++) {
                initialValuesCloned[j] = initialValues[j].clone();
            }
            Individual initialRandom = Individual.getInitialRandom(fitness, initialValuesCloned, floats, negatives);
            this.addIndividual(initialRandom);
        }
    }

    public int size() {
        return list.size();
    }

    public Stream<Individual> stream() {
        return list.stream();
    }

    @NotNull
    @Override
    public Iterator<Individual> iterator() {
        return list.iterator();
    }

    public ListIterator<Individual> reverseIterator() {
        return list.listIterator(list.size());
    }

    public void sort() {
        Collections.sort(list);
    }

    public Individual get(int index) {
        return list.get(index);
    }

    public Individual removeRandom() {
        return list.remove(new Random().nextInt(list.size()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Population)) return false;

        Population pop = (Population) obj;
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getFitness() != pop.list.get(i).getFitness()){
                return false;
            }
        }
        return true;
        //return pop.list.equals(this.list);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Individual ind : this) {
            result.append("\t");
            result.append(ind.getFitness());
            result.append("->");
            result.append(Arrays.toString(ind.getParameters()));
            result.append("\n");
        }
        return result.toString();
    }
}

