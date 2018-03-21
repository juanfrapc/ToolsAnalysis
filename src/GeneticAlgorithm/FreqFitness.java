package GeneticAlgorithm;

import Application.AligningTask;
import Application.BWAMEMTask;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.AlignmentsStatistics;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FreqFitness extends Fitness{

    private final String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_1.fastq.gz";
    private final String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_2.fastq.gz";
    private final String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";
    private final String name;
    private final AligningTask task;

    FreqFitness( AligningTask task, String name) {
        this.name = name;
        this.task = task;
    }

    public float eval(Individual individual){

        if (Fitness.contains(individual)) {
            float fitness = Fitness.getFitness(individual);
            System.out.println(Arrays.toString(individual.getParameters()) + ":Fitness ya calculado " + fitness);
            return fitness;
        }

        task.setParameters(individual.getParameters());
        AlignmentsStatistics stats = task.run();
        final AtomicLong result = new AtomicLong(stats.getUniquelyMapped());
        Map<Integer, Long> mapQ = stats.getMultiplyMapQDistribution();
        mapQ.entrySet().stream().parallel().
                forEach(entry -> {
                    if (entry.getKey() >= 20) result.addAndGet(entry.getValue());
                });
        float value = result.get() / (float) stats.getTotal();
        System.out.println(name + "("+ Arrays.toString(individual.getParameters()) +"): Fitness calculado con éxito = " + value);
        Fitness.put(individual, value);
        return value;
    }

}
