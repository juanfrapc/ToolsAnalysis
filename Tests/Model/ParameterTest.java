package Model;

import GeneticAlgorithm.Algorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest {

    @Test
    void updateRandom() {
        for (int i = 0; i < 10; i++) {
            Parameter parm = new Parameter('n', "0.04");
            parm.updateRandom(Algorithm.getFloats("ALN"), Algorithm.getNegatives("ALN"));
            System.out.println(parm.getValue());

        }
    }
}