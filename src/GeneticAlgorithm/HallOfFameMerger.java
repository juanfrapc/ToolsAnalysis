package GeneticAlgorithm;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;

import java.util.Iterator;

public class HallOfFameMerger  implements PopulationMerger{

    @Override
    public Population merge(Population pop1, Population pop2, int size) {
        pop1.sort();
        pop2.sort();
        Population result = new Population();
        Iterator<Individual> iterator1 = pop1.iterator();
        Iterator<Individual> iterator2 = pop2.iterator();
        int count = 0;
        Individual ind1 = iterator1.next();
        Individual ind2 = iterator2.next();
        while(iterator1.hasNext() && iterator2.hasNext() && count<size){
            if (ind1.compareTo(ind2)<=0){
                result.addIndividual(ind1);
                ind1=iterator1.next();
                count++;
            }else{
                result.addIndividual(ind2);
                ind2=iterator2.next();
                count++;
            }
        }
        return result;

    }
}
