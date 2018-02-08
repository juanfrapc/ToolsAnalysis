package GeneticAlgorithm.Model;

import java.util.function.Supplier;

public class FitnessLambda extends Fitness{

    private final Supplier<? extends Float> supplier;
    public FitnessLambda(Supplier<? extends Float> supplier)
    {
        this.supplier = supplier;
    }


    @Override
    public float eval(Individual individual) {
        return supplier.get();
    }
}
