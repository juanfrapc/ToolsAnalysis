package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;
import View.StatWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

public class FileStatsWriter implements StatWriter {

    private final String path;

    FileStatsWriter(String path) {
        this.path = path;
    }

    @Override
    public void write(Parameter[] parameters, AlignmentsStatistics statistics) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))){
            writer.write("Estadísitcas:\n");
            writer.write("Parameters: " + Arrays.toString(parameters)+"\n");
            writer.write("-------------------------\n");
            long total = statistics.getTotal();
            long uniquelyMapped = statistics.getUniquelyMapped();
            long unmapped = statistics.getUnmapped();
            long multiplyMapped = statistics.getMultiplyMapped();
            long yAlln = statistics.getYChromosomeAlignments();

            writer.write(String.format("Número de alineamientos totales: %d\n", total));
            writer.write(String.format("Número de alineamientos mapeados 1 vez: %d (%.2f %%)\n",
                    uniquelyMapped, uniquelyMapped/(double)total * 100));
            writer.write(String.format("Número de alineamientos no mapeados: %d (%.2f %%)\n",
                    unmapped, unmapped/(double)total * 100));
            writer.write(String.format("Número de alineamientos múltiples: %d (%.2f %%)\n",
                    multiplyMapped, multiplyMapped/(double)total * 100));
            writer.write(String.format("Número de alineamientos en cromosoma y: %d \n", yAlln));
            writer.write("--------------------------------------------\n");
            writer.write("Distribuciónes de MapQ\n");

            writer.write("\t");
            for (int i = 0; i <254; i++) {
                writer.write("\t" + i);
            }
            writeDistribution(writer, "Total", statistics.getTotalMapQDistribution());
            writeDistribution(writer, "Unmap", statistics.getUnmappedMapQDistribution());
            writeDistribution(writer, "Multi", statistics.getMultiplyMapQDistribution());
            writeDistribution(writer, "Uniq", statistics.getUniquelyMapQDistribution());
            writer.write("\n--------------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDistribution(BufferedWriter writer, String name,  Map<Integer, Long> distribution) throws IOException {

        writer.write("\n");
        writer.write(name);
        for (int i = 0; i <distribution.size(); i++) {
            writer.write("\t" + (distribution.containsKey(i)?distribution.get(i):0));
        }

    }
}
