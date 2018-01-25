package View;

import Model.AlignmentsStatistics;

public class ConsoleStatsWriter  implements StatWriter{


    @Override
    public void write(AlignmentsStatistics statistics) {
        System.out.println("Estadísitcas:");
        System.out.println("-------------------------");
        long total = statistics.getTotal();
        long uniquelyMapped = statistics.getUniquelyMapped();
        long unmapped = statistics.getUnmapped();
        long multiplyMapped = statistics.getMultiplyMapped();

        System.out.printf("Número de alineamientos totales: %d\n", total);
        System.out.printf("Número de alineamientos mapeados 1 vez: %d (%.2f %%)\n", uniquelyMapped, uniquelyMapped/(double)total * 100);
        System.out.printf("Número de alineamientos no mapeados: %d (%.2f %%)\n", unmapped, unmapped/(double)total * 100);
        System.out.printf("Número de alineamientos múltiples: %d (%.2f %%)\n", multiplyMapped, multiplyMapped/(double)total * 100);
        System.out.println("--------------------------------------------\n");
    }
}
