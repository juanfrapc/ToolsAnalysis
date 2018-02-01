package GeneticAlgorithm;

import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Model.Population;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CrossoverTest {

    private Individual father;
    private Individual mother;

    @BeforeEach
    void initFake() {
        father = Individual.getInitialRamdom("father", individual -> 0);
        mother = Individual.getInitialRamdom("mother", individual -> 0);
    }

    @Test
    void reproduceSP() {
        SPCrossover reproducer = new SPCrossover(individual -> 0);
        Individual[] offspring = reproducer.reproduce(father, mother);
        Parameter[] fatherParam = father.getParameters();
        Parameter[] motherParam = mother.getParameters();
        Parameter[] child1Param = offspring[0].getParameters();
        Parameter[] child2Param = offspring[1].getParameters();
        int cross = 0;
        while (cross < fatherParam.length && fatherParam[cross].equals(child1Param[cross]) ) {
            cross++;
        }
        for (int i = 0; i < child1Param.length; i++) {
            if (i < cross) {
                assertEquals(fatherParam[i], child1Param[i], "Error in child 1 father");
                assertEquals(motherParam[i], child2Param[i], "Error in child 2 mother");
            } else {
                assertEquals(motherParam[i], child1Param[i], "Error in child 1 mother");
                assertEquals(fatherParam[i], child2Param[i], "Error in child 2 father");
            }
        }

    }

}