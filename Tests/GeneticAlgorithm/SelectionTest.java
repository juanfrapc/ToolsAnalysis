package GeneticAlgorithm;

import GeneticAlgorithm.Model.FitnessLambda;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import GeneticAlgorithm.Operators.Selection;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class SelectionTest {

    private Population population;

    @BeforeEach
    void initFake() {
        population = new Population();
        Parameter[] fakeParm= Algorithm.getInitialValues("MEM");
        for (int i = 1; i < 11; i++) {
            Individual fakeInd = new Individual(fakeParm, new FitnessLambda(()->0f));
            fakeInd.setFitness(i);
            population.addIndividual(fakeInd);
        }
    }

    @Test
    void roulette() throws CloneNotSupportedException {
        int n = 2000;
        Population selected = Selection.roulette(population, n);
        HashMap<Float, Integer> freq = new HashMap<>();
        for (Individual ind : selected) {
            float fitness = ind.getFitness();
            if (freq.containsKey(fitness)) {
                freq.put(fitness, freq.get(fitness)+1);
            } else {
                freq.put(fitness, 1);
            }
        }
        for (Map.Entry<Float, Integer> entry : freq.entrySet()) {
            for (Map.Entry<Float, Integer> compare : freq.entrySet())
                if (entry.getKey() <= compare.getKey()) assert (entry.getValue() <= compare.getValue());
                else assert (entry.getValue() > compare.getValue());
        }
    }

}