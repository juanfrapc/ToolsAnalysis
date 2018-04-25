package GeneticAlgorithm.Model;

import GeneticAlgorithm.Algorithm;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class PopulationTest {

    private Population pop1;
    private Population pop2;
    private Population pop3;

    @BeforeEach
    void init() throws CloneNotSupportedException {
        pop1 = new Population();
        pop2 = new Population();
        pop3 = new Population();

        Parameter[] example1 = Algorithm.getInitialValues("MEM");
        Parameter[] example2 = Algorithm.getInitialValues("SW");
        Parameter[] example3 = Algorithm.getInitialValues("ALN");
        Individual individual1 = new Individual(example1, 0);
        Individual individual2 = new Individual(example2, 1);
        Individual individual3 = new Individual(example3, 2);

        pop1.addIndividual(individual1);
        pop1.addIndividual(individual2);

        pop2.addIndividual(individual1.clone());
        pop2.addIndividual(individual2.clone());

        pop3.addIndividual(individual1);
        pop3.addIndividual(individual3);
    }

    @Test
    void equals() {
        assertEquals(pop1, pop2, "Population 1 and 2 not equal");
        assertNotEquals(pop1, pop3, "Population 1 and 3 are equal");
        assertNotEquals(pop2, pop3, "Population 2 and 3 are equal");
    }

}