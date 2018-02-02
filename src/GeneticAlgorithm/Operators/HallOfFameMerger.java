package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;

import java.util.Iterator;

public class HallOfFameMerger implements PopulationMerger {

    private Population result;

    @Override
    public Population merge(Population pop1, Population pop2, int size) {
        pop1.sort();
        pop2.sort();
        result = new Population();
        Iterator<Individual> iterator1 = pop1.iterator();
        Iterator<Individual> iterator2 = pop2.iterator();
        int count = 0;
        if (!iterator1.hasNext()) {
            fillWith(iterator2, count, size);
            return result;
        } else if (!iterator2.hasNext()) {
            fillWith(iterator1, count, size);
            return result;
        }
        Individual ind1 = iterator1.next();
        Individual ind2 = iterator2.next();
        while (count < size) {
            if (ind1.compareTo(ind2) <= 0) {
                result.addIndividual(ind1);
                count++;
                if (iterator1.hasNext()) ind1 = iterator1.next();
                else break;
            } else {
                result.addIndividual(ind2);
                count++;
                if (iterator2.hasNext()) ind2 = iterator2.next();
                else break;
            }
        }
        if (count >= size) return result;
        if (!iterator1.hasNext()) {
            fillWith(iterator2, count, size);
        }
        if (!iterator2.hasNext()) {
            fillWith(iterator1, count, size);
        }
        return result;

    }

    private void fillWith(Iterator<Individual> iterator, int count, int size) {

        while (iterator.hasNext() && count < size) {
            result.addIndividual(iterator.next());
            count++;
        }
    }
}
