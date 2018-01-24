package Application;

import Model.AlignmentStatistics;
import Model.Parameter;
import Parser.ProcessFileStats;

import java.io.File;
import java.io.IOException;

public class AlignerTask implements Runnable {

    private String name;
    private String forwardPath;
    private String reversePath;
    private String reference;
    private String outFile;
    private String statsFile;
    private AlignmentStatistics statistics;
    private Parameter[] parameters;

    AlignerTask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
        this.name = name;
        this.forwardPath = forwardPath;
        this.reversePath = reversePath;
        this.reference = reference;
        this.parameters = parameters;
        this.outFile = "/media/uichuimi/DiscoInterno/Juanfra/" + name + ".bam";
        this.statsFile = "/media/uichuimi/DiscoInterno/Juanfra/" + name + ".stat";
        this.statistics = new AlignmentStatistics();
    }

    @Override
    public void run() {

        BWAligner bwa = new BWAligner(forwardPath, reversePath, reference, outFile, parameters);
        System.out.println(name + ": Start running");
        Process run;
        try {
            run = bwa.run();
            int error = run.waitFor();
            if (error == 0) System.out.println(name + ": alineamiento terminado con éxito");
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(name + ": Obtaining stats");
        boolean process = ProcessFileStats.process(new File(outFile), statistics);
        if (process) System.out.println(name + ": Procesamiento terminado con éxito");
        else System.err.println(name + ": Error de procesado de estadisticas");

        FileStatsWriter writer = new FileStatsWriter(statsFile);
        writer.write(statistics);

    }
}
