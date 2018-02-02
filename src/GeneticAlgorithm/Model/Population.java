package GeneticAlgorithm.Model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class Population implements Iterable<Individual>{

    private ArrayList<Individual> list;

    public Population() {
        list = new ArrayList<>();
    }

    public void addIndividual(Individual ind){
        list.add(ind);
    }

    public void initilize(int size, Fitness fitness){
        for (int i = 0; i < size; i++) {
            this.addIndividual(Individual.getInitialRamdom(fitness));
        }
    }

    public int size() {
        return list.size();
    }

    public Stream<Individual> stream() {
        return list.stream();
    }

    @Override
    public Iterator<Individual> iterator() {
        return list.iterator();
    }

    public void sort(){
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
        if(obj == null) return false;
        if (!(obj instanceof Population)) return false;

        Population pop = (Population) obj;
        return pop.list.equals(this.list);
    }

}

