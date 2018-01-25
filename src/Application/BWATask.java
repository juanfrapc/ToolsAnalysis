package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;
import Model.Timer;
import Parser.ProcessFileStats;

import java.io.File;

public class BWATask implements Runnable {

    private String name;
    private String forwardPath;
    private String reversePath;
    private String reference;
    private String outFile;
    private String statsFile;
    private AlignmentsStatistics statistics;
    private Parameter[] parameters;

    BWATask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
        this.name = name;
        this.forwardPath = forwardPath;
        this.reversePath = reversePath;
        this.reference = reference;
        this.parameters = parameters;
        this.outFile = "/media/uichuimi/DiscoInterno/Juanfra/" + name + ".bam";
        this.statsFile = "/media/uichuimi/DiscoInterno/Juanfra/" + name + ".stat";
        this.statistics = new AlignmentsStatistics();
    }

    @Override
    public void run() {

        BWAligner bwa = new BWAligner(forwardPath, reversePath, reference, outFile, parameters);
        this.name = bwa.getID() + "-" + name;
        Timer timer = new Timer();
        timer.start();
        System.out.println(name + ": Start running");
        Process run;
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.reset();
        System.out.println(name + ": Obtaining stats");
        boolean process = ProcessFileStats.process(new File(outFile), statistics);
        timer.stop();
        if (process) System.out.println(name + ": Procesamiento terminado con éxito. " + timer.report());
        else System.err.println(name + ": Error de procesado de estadisticas");

        FileStatsWriter writer = new FileStatsWriter(statsFile);
        writer.write(statistics);

    }
}
