package GeneticAlgorithm.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class Fitness {

    private static final Map<Individual, Float> map = new HashMap<>();

    public abstract float eval(Individual individual);

    static boolean contains(Individual key) {
        return map.containsKey(key);
    }

    static void clearMap() {
        map.clear();
    }

    static float getFitness(Individual key) {
        return map.get(key);
    }

    static void put(Individual key) {
        map.put(key, key.getFitness());
    }
}
