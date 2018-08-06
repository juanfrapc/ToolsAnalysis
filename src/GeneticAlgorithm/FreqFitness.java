package GeneticAlgorithm;

import Application.AlignningStatsTasks.AligningTask;
import GeneticAlgorithm.Model.Fitness;
import GeneticAlgorithm.Model.Individual;
import Model.AlignmentsStatistics;
import Model.Parameter;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FreqFitness extends Fitness{

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
        if (task.getName().equals("MEM")) {
            Parameter[] runningParam = new Parameter[individual.getParameters().length];
            System.arraycopy(individual.getParameters(), 0, runningParam, 0, runningParam.length - 1);
            runningParam[runningParam.length - 1] = new Parameter('p', "");
            task.setParameters(runningParam);
        }
        AlignmentsStatistics stats = task.run();

        if (stats== null) return 0;
//        final AtomicLong result = new AtomicLong(stats.getUniquelyMapped());
        final AtomicLong result = new AtomicLong(0);
        Map<Integer, Long> mapQUniq = stats.getUniquelyMapQDistribution();
        mapQUniq.entrySet().stream().parallel().
                forEach(entry -> {
                    if (entry.getKey() >= 20) result.addAndGet(entry.getValue());
                });
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
