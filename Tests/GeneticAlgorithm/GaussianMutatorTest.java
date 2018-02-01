package GeneticAlgorithm;

import GeneticAlgorithm.Model.Individual;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GaussianMutatorTest {

    private Individual ind;
    private int n = 100000;

    @BeforeEach
    void initFake() {
        ind = Individual.getInitialRamdom("father", individual -> 0);
    }

    @Test
    void mutate() throws CloneNotSupportedException {
        Mutator mutator = new GaussianMutator((float) 0.05);
        Parameter[] parameter = ind.getParameters();
        int count = 0;

        for (int i = 0; i < n; i++) {
            Individual aux = ind.clone();
            mutator.mutate(aux);
            Parameter[] auxParam = aux.getParameters();
            for (int j = 1; j < parameter.length; j++) {
                if (!parameter[j].equals(auxParam[j])) count++;
            }
        }
        assert(Math.abs(0.05-count/count/((float)11*n))<2);
    }

}