package GeneticAlgorithm.Model;

import GeneticAlgorithm.Algorithm;
import Model.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static GeneticAlgorithm.Model.Fitness.put;
import static java.nio.charset.StandardCharsets.US_ASCII;
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
        put(individual, individual.getFitness());

    }

    @Test
    void contains() {
        assert(Fitness.contains(individual));
        assert (individual.equals(clone));
        assert(Fitness.contains(clone));
        assertEquals(1, Fitness.size(), "Elements cardinaity not matching.");
        assertFalse(Fitness.contains(individualNull));
        assertFalse(Fitness.contains(individual2));
        put(individual2, individual2.getFitness());
        put(individualNull, individualNull.getFitness());
        assertEquals(15, Fitness.getFitness(individual), "Error in fitness");
        assertEquals(15, Fitness.getFitness(clone), "Error in fitness");
        assertEquals(20, Fitness.getFitness(individual2), "Error in fitness");
        assertEquals(0, Fitness.getFitness(individualNull), "Error in fitness");
    }

    @Test
    void loadKnowledge() throws FileNotFoundException {
        File file = new File("/home/juanfrapc/GENOME_DATA/outDAMALN.txt.filtrado");
        Parameter[] defaultValues = Algorithm.getInitialValues("ALN");
        Fitness.loadBWAKnowledge(file,defaultValues);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), US_ASCII));
        reader.lines().map((String line) -> line.trim().split("\t")).map((String[] line) -> new Individual(line, defaultValues))
                .filter((Individual ind )-> Fitness.contains(ind)).forEach((Individual ind)-> {assert(Fitness.contains(ind));});
        Individual initial = new Individual(Algorithm.getInitialValues("ALN"), 0);
        assert(Fitness.contains(initial));
    }
}