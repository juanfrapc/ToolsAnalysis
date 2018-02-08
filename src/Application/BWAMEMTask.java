package Application;

import Application.Aligners.BWAMEMAligner;
import Model.AlignmentsStatistics;
import Model.Parameter;
import Model.Timer;
import Parser.ParseFileStats;

import java.io.File;
import java.util.Date;

public class BWAMEMTask extends Thread{

    private String name;
    private String forwardPath;
    private String reversePath;
    private String reference;
    private String outFile;
    private String logFile;
    private String statsFile;
    private AlignmentsStatistics statistics;
    private Parameter[] parameters;

    public BWAMEMTask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
        this.name = name;
        this.forwardPath = forwardPath;
        this.reversePath = reversePath;
        this.reference = reference;
        this.parameters = parameters;
        this.outFile = "/media/uichuimi/DiscoInterno/Juanfra/sams/" + name + ".sam";
        this.logFile = "/media/uichuimi/DiscoInterno/Juanfra/logs/" + name + ".log";
        this.statsFile = "/media/uichuimi/DiscoInterno/Juanfra/stats/" + name + ".stat";
        this.statistics = new AlignmentsStatistics();
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        BWAMEMAligner bwa = new BWAMEMAligner(forwardPath, reversePath, reference, outFile, logFile, parameters);
        this.name = bwa.getID() + "-" + name;
        timer.start();

        System.out.println("(" + new Date().toString() + ") " + name + ": Start running");
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
        boolean process = ParseFileStats.process(new File(outFile), statistics);
        timer.stop();
        if (process) System.out.println(name + ": Procesamiento terminado con éxito. " + timer.report());
        else System.err.println(name + ": Error de procesado de estadisticas");

        FileStatsWriter writer = new FileStatsWriter( statsFile);
        writer.write(parameters, statistics);

    }
}
