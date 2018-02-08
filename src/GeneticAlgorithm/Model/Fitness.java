package GeneticAlgorithm.Model;

import Model.Parameter;

import java.util.HashMap;
import java.util.Map;

public abstract class Fitness {

    private static Map<Individual, Float> map = new HashMap();

    public abstract float eval(Individual individual);

    public static boolean contains(Individual key) {
        return map.containsKey(key);
    }

    public static void clearMap() {
        map.clear();
    }

    public static float getFitness(Object key) {
        return map.get(key);
    }

    public static Float put(Individual key) {
        return map.put(key, key.getFitness());
    }
}
