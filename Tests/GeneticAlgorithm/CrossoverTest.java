package GeneticAlgorithm;

import GeneticAlgorithm.Model.FitnessLambda;
import GeneticAlgorithm.Model.Individual;
import GeneticAlgorithm.Operators.SPCrossover;
import Model.Parameter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrossoverTest {

    private Individual father;
    private Individual mother;


    @BeforeEach
    void initFake() {
        father = Individual.getInitialRandom(new FitnessLambda(()->0f),
                Algorithm.getInitialValues("MEM"),
                Algorithm.getFloats("MEM"),
                Algorithm.getNegatives("MEM")
                );
        mother = Individual.getInitialRandom(new FitnessLambda(()->0f),
                Algorithm.getInitialValues("MEM"),
                Algorithm.getFloats("MEM"),
                Algorithm.getNegatives("MEM")
        );
    }

    @Test
    void reproduceSP() {
        SPCrossover reproducer = new SPCrossover(new FitnessLambda(()->0f));
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