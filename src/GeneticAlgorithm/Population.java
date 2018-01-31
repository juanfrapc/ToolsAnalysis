package GeneticAlgorithm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

public class Population implements Iterable<Individual>{

    private ArrayList<Individual> list;

    public Population() {
        list = new ArrayList<>();
    }

    public boolean addIndividual(Individual ind){
        return list.add(ind);
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
}

