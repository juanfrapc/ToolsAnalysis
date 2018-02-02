package GeneticAlgorithm;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.HallOfFameMerger;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class HallOfFameMergerTest {

    private Population pop1;
    private Population pop2;
    private HallOfFameMerger merger;

    @BeforeEach
    void init() {
        merger = new HallOfFameMerger();
        pop1 = new Population();
        pop2 = new Population();
    }

    void fillPop(int n, int factor) {
        for (int i = 0; i < n; i++) {
            int finalI = i;
            Individual ind = new Individual(new Parameter[0], individual -> finalI);
            pop1.addIndividual(ind);
            Individual ind1 = new Individual(new Parameter[0], individual -> (finalI + 1) * factor);
            pop2.addIndividual(ind1);
        }
    }

    private void printPop(Population result) {
        Iterator<Individual> resIterator = result.iterator();
        Iterator<Individual> pop1Iterator = pop1.iterator();
        Iterator<Individual> pop2Iterator = pop2.iterator();
        System.out.println("--------------------");
        while (resIterator.hasNext() && pop1Iterator.hasNext() && pop2Iterator.hasNext()) {
            Individual resNext = resIterator.next();
            Individual pop1Next = pop1Iterator.next();
            Individual pop2Next = pop2Iterator.next();
            System.out.println(resNext.getFitness() + " - " + pop1Next.getFitness() + " - " + pop2Next.getFitness());
            assert (resNext.getFitness() <= pop1Next.getFitness() && resNext.getFitness() <= pop2Next.getFitness());
        }
    }

    @Test
    void mergeResultOrdered() {
        fillPop(10, 2);
        Population result = merger.merge(pop1, pop2, 10);
        assertEquals(10, result.size());
        printPop(result);
    }


    @Test
    void mergeResultExtremePop1() {
        fillPop(10, 10);
        Population result = merger.merge(pop1, pop2, 10);
        printPop(result);
        assertEquals(10, result.size());
        Iterator<Individual> resIterator = result.iterator();
        Iterator<Individual> pop1Iterator = pop1.iterator();
        while (resIterator.hasNext() && pop1Iterator.hasNext()) {
            Individual resNext = resIterator.next();
            Individual pop1Next = pop1Iterator.next();
            assertEquals(pop1Next.getFitness(), resNext.getFitness());
        }
    }

    @Test
    void mergeResultExtremePop2() {
        fillPop(10, -1);
        Population result = merger.merge(pop1, pop2, 10);
        assertEquals(10, result.size());
        printPop(result);
        Iterator<Individual> resIterator = result.iterator();
        Iterator<Individual> pop2Iterator = pop2.iterator();
        while (resIterator.hasNext() && pop2Iterator.hasNext()) {
            Individual resNext = resIterator.next();
            Individual pop1Next = pop2Iterator.next();
            assertEquals(pop1Next.getFitness(), resNext.getFitness());
        }
    }

    @Test
    void mergeResultBigger() {
        fillPop(10, 2);
        Population result = merger.merge(pop1, pop2, 15);
        assertEquals(15, result.size());
        printPop(result);
    }

    @Test
    void mergeResultSmaller() {
        fillPop(10, 2);
        Population result = merger.merge(pop1, pop2, 5);
        assertEquals(5, result.size());
        printPop(result);
    }

}