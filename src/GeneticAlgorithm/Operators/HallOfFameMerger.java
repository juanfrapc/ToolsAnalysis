package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;

import java.util.ListIterator;

public class HallOfFameMerger implements PopulationMerger {

    private Population result;

    @Override
    public Population merge(Population pop1, Population pop2, int size) {
        pop1.sort();
        pop2.sort();
        result = new Population();
        ListIterator<Individual> iterator1 = pop1.reverseIterator();
        ListIterator<Individual> iterator2 = pop2.reverseIterator();
        int count = 0;
        if (!iterator1.hasPrevious()) {
            fillWith(iterator2, count, size);
            result.sort();
            return result;
        } else if (!iterator2.hasPrevious()) {
            fillWith(iterator1, count, size);
            result.sort();
            return result;
        }
        Individual ind1 = iterator1.previous();
        Individual ind2 = iterator2.previous();
        while (count < size) {
            if (ind1.compareTo(ind2) >= 0) {
                result.addIndividual(ind1);
                count++;
                if (iterator1.hasPrevious()) ind1 = iterator1.previous();
                else break;
            } else {
                result.addIndividual(ind2);
                count++;
                if (iterator2.hasPrevious()) ind2 = iterator2.previous();
                else break;
            }
        }
        result.sort();
        System.out.println("Padres:\n" + pop1);
        System.out.println("Hijos:\n" + pop2);
        System.out.println("Mejores:\n" + result);
        if (count >= size) {
            return result;
        }
        if (!iterator1.hasPrevious()) {
            fillWith(iterator2, count, size);
        }
        if (!iterator2.hasPrevious()) {
            fillWith(iterator1, count, size);
        }
        result.sort();
        System.out.println("Padres:\n" + pop1);
        System.out.println("Hijos:\n" + pop2);
        System.out.println("Mejores:\n" + result);
        return result;

    }

    private void fillWith(ListIterator<Individual> iterator, int count, int size) {

        while (iterator.hasPrevious() && count < size) {
            result.addIndividual(iterator.previous());
            count++;
        }
    }
}
