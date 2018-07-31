package GeneticAlgorithm;

import org.junit.jupiter.api.Test;

import static GeneticAlgorithm.Algorithm.applyEvolution;

class AlgorithmMainTest {


    @Test
    void applyEvolutionTest() {
        String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
        String forward = "Tests/tutorialFile/altalt.read1.fq";
        String reverse = "Tests/tutorialFile/altalt.read2.fq";
        try {
            applyEvolution("MEM", forward, reverse, reference);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
    }
}