package GeneticAlgorithm.Operators;

import GeneticAlgorithm.Model.Population;

import java.util.Random;

public class Selection {

    public static Population roulette(Population origin, int size) throws CloneNotSupportedException {

        Population selected = new Population();
        final float[] totalFitness = {0};
        origin.stream().forEach(individual -> totalFitness[0] += individual.getFitness());

        Random random = new Random();
        origin.sort();
        for (int i = 0; i < size; i++) {
            float rand = random.nextFloat() * totalFitness[0];
            int selection = -1;
            float flag = 0;
            do {
                flag += origin.get(++selection).getFitness();
            } while (flag<rand && selection<origin.size());
            selected.addIndividual(origin.get(selection).clone());
        }
        return selected;

    }

}
