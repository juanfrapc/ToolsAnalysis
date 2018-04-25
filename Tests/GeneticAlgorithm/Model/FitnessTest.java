package GeneticAlgorithm.Model;

import GeneticAlgorithm.Algorithm;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FitnessTest {

    private Individual individual;
    private Individual clone;
    private Individual individualNull;
    private Individual individual2;

    @BeforeEach
    void init() throws CloneNotSupportedException {
        Fitness.clearMap();
        Parameter[] param = Algorithm.getInitialValues("MEM");
        Parameter[] param2 = Algorithm.getInitialValues("ALN");
        individual = new Individual(param, 15f);
        individual2 = new Individual(param2, 20f);
        individualNull = new Individual(new Parameter[0], 0f);
        clone = individual.clone();
        Fitness.put(individual, individual.getFitness());

    }

    @Test
    void contains() {
        assert(Fitness.contains(individual));
        assert (individual.equals(clone));
        assert(Fitness.contains(clone));
        assertEquals(1, Fitness.size(), "Elements cardinaity not matching.");
        assertFalse(Fitness.contains(individualNull));
        assertFalse(Fitness.contains(individual2));
        Fitness.put(individual2, individual2.getFitness());
        Fitness.put(individualNull, individualNull.getFitness());
        assertEquals(15, Fitness.getFitness(individual), "Error in fitness");
        assertEquals(15, Fitness.getFitness(clone), "Error in fitness");
        assertEquals(20, Fitness.getFitness(individual2), "Error in fitness");
        assertEquals(0, Fitness.getFitness(individualNull), "Error in fitness");
    }

}