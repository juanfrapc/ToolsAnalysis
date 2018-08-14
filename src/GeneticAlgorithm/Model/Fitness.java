package GeneticAlgorithm.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class Fitness {

    private static final Map<Individual, Values> map = new HashMap<>();

    public abstract float eval(Individual individual);

    protected static boolean contains(Individual key) {
        return map.containsKey(key);
    }

    public static boolean containsVCF(Individual key) {
        return map.get(key).getVcfFitness() != 0;
    }

    public static void clearMap() {
        map.clear();
    }

    protected static float getFitness(Individual key) {
        return map.get(key).getBamFitness();
    }

    public static float getVCFFitness(Individual key) {
        return map.get(key).getVcfFitness();
    }

    protected static void put(Individual key, float fitness) {
        Values values = new Values(fitness);
        map.put(key, values);
    }

    public static void putVCF(Individual key, float fitness) {
        map.get(key).setVcfFitness(fitness);
    }

    static int size() {
        return map.size();
    }


}

class Values {
    private final float bamFitness;
    private float vcfFitness;

    Values(float bamFitness) {
        this.bamFitness = bamFitness;
    }

    float getBamFitness() {
        return bamFitness;
    }

    float getVcfFitness() {
        return vcfFitness;
    }

    void setVcfFitness(float vcfFitness) {
        this.vcfFitness = vcfFitness;
    }
}

