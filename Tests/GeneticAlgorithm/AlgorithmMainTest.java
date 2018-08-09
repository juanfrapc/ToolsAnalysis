package GeneticAlgorithm;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static GeneticAlgorithm.Algorithm.applyEvolution;

class AlgorithmMainTest {


    @Test
    void applyEvolutionTest() {
        String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
        String forward = "Tests/tutorialFile/altalt.read1.fq";
//        String forward = "Tests/tutorialFile/altalt_interleaved.fq.gz";
        String reverse = "Tests/tutorialFile/altalt.read2.fq";
        try {
            applyEvolution("ALN", forward, reverse, reference, forward);
        } catch (CloneNotSupportedException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}