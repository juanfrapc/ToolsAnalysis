package Application;

import Model.Parameter;
import Model.Statistics;
import Model.VariantStatistics;
import View.StatWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileVariantStatsWriter implements StatWriter {

    private final String path;

    public FileVariantStatsWriter(String path) {
        this.path = path;
    }

    @Override
    public void write(Parameter[] parameters, Statistics originalStatistics) {
        if (! (originalStatistics instanceof VariantStatistics)) return;
        VariantStatistics statistics = (VariantStatistics) originalStatistics;
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))){
            writer.write("Estadísitcas:\n");
            writer.write("Parameters: " + Arrays.toString(parameters)+"\n");
            writer.write("-------------------------\n");
            long total = statistics.getTotal();
            long falsePositive = statistics.getFalsePositive();

            writer.write(String.format("Número de variantes totales: %d\n", total));
            writer.write(String.format("Número de variantes Falso positivo: %d \n", falsePositive));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

