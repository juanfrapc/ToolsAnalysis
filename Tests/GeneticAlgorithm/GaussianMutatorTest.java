package GeneticAlgorithm;

import GeneticAlgorithm.Model.FitnessLambda;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Operators.GaussianMutator;
import GeneticAlgorithm.Operators.Mutator;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GaussianMutatorTest {

    private Individual ind;

    @BeforeEach
    void initFake() {
        ind = Individual.getInitialRamdom(new FitnessLambda(()->0f));
    }

    @Test
    void mutate() throws CloneNotSupportedException {
        Mutator mutator = new GaussianMutator((float) 0.05);
        Parameter[] parameter = ind.getParameters();
        int count = 0;

        int n = 100000;
        for (int i = 0; i < n; i++) {
            Individual aux = ind.clone();
            mutator.mutate(aux, new FitnessLambda(()->0f));
            Parameter[] auxParam = aux.getParameters();
            for (int j = 1; j < parameter.length; j++) {
                if (!parameter[j].equals(auxParam[j])) count++;
            }
        }
        assert(Math.abs(0.05-count/count/((float)11* n))<2);
    }

}