package GeneticAlgorithm.Model;

import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IndividualTest {

    private Individual individual1;
    private Individual individual2;
    private Individual individual3;
    private Individual individual4;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        Parameter[] example1 = new Parameter[]{new Parameter('r', String.valueOf(random.nextFloat())), new Parameter('b', "1")};
        Parameter[] example2 = new Parameter[]{new Parameter('a', "2"), new Parameter('b', "2")};
        individual1 = new Individual(example1, 0);
        individual2 = new Individual(example2, 1);
        individual3 = new Individual(example1, 0);
        individual4 = new Individual(example1, 1);
    }

    @Test
    void equals() throws CloneNotSupportedException {
        assertEquals(individual1, individual1.clone());
        assertEquals(individual1, individual3);

        assertNotEquals(individual1, individual2);
        assertNotEquals(individual3, individual2);
        assertNotEquals(individual1, individual4);
        assertNotEquals(individual3, individual4);
    }

    @Test
    void hashCodeTest() throws CloneNotSupportedException {
        assertEquals(individual1.hashCode(),individual1.clone().hashCode());
        assertEquals(individual1.hashCode(), individual3.hashCode());

        assertNotEquals(individual1.hashCode(), individual2.hashCode());
        assertNotEquals(individual3.hashCode(), individual2.hashCode());

        assertEquals(individual1.hashCode(), individual4.hashCode());
    }

}
