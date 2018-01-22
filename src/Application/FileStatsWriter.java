package Application;

import Model.AlignmentStatistics;
import View.StatWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStatsWriter implements StatWriter {

    private String path;

    FileStatsWriter(String path) {
        this.path = path;
    }

    @Override
    public void write(AlignmentStatistics statistics) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))){
            writer.write("Estadísitcas:\n");
            writer.write("-------------------------\n");
            long total = statistics.getTotal();
            long uniquelyMapped = statistics.getUniquelyMapped();
            long unmapped = statistics.getUnmapped();
            long multiplyMapped = statistics.getMultiplyMapped();

            writer.write(String.format("Número de alineamientos totales: %d\n", total));
            writer.write(String.format("Número de alineamientos mapeados 1 vez: %d (%.2f %%)\n",
                    uniquelyMapped, uniquelyMapped/(double)total * 100));
            writer.write(String.format("Número de alineamientos no mapeados: %d (%.2f %%)\n",
                    unmapped, unmapped/(double)total * 100));
            writer.write(String.format("Número de alineamientos múltiples: %d (%.2f %%)\n",
                    multiplyMapped, multiplyMapped/(double)total * 100));
            writer.write("--------------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
