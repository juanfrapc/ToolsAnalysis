package GeneticAlgorithm;

import Model.AlignmentsStatistics;
import Control.Sam2StatsParser;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class FreqFitnessTest {


    @Ignore("too long in time")
    @Test
    void eval() {
        String name = "default";
        AlignmentsStatistics stats = new AlignmentsStatistics();
        Sam2StatsParser.process(new File("Tests/ejemplo.sam"), stats);
        final AtomicLong result = new AtomicLong(stats.getUniquelyMapped());
        Map<Integer, Long> mapQ = stats.getMultiplyMapQDistribution();
        mapQ.entrySet().stream().parallel().
                forEach(entry -> {
                    if (entry.getKey() >= 20) result.addAndGet(entry.getValue());
                });
        float value = result.get() / (float) stats.getTotal();
        System.out.println(name + ": Fitness calculado con éxito = " + value);

    }


}