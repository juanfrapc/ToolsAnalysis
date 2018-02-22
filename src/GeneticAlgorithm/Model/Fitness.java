package GeneticAlgorithm.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class Fitness {

    private static final Map<Individual, Float> map = new HashMap<>();

    public abstract float eval(Individual individual);

    protected static boolean contains(Individual key) {
        return map.containsKey(key);
    }

    public static void clearMap() {
        map.clear();
    }

    protected static float getFitness(Individual key) {
        return map.get(key);
    }

    protected static void put(Individual key, float fitness) {
        map.put(key, fitness);
    }

    static int size() {
        return map.size();
    }
}
