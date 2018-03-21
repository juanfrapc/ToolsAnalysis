package Application;

import Application.Aligners.BWASWAligner;
import Model.AlignmentsStatistics;
import Model.Parameter;
import Model.Timer;
import View.Parser.Sam2StatsParser;

import java.io.File;
import java.util.Date;

public class BWASWTask implements AligningTask {

    private String name;
    private final String forwardPath;
    private final String reversePath;
    private final String reference;
    private final String outFile;
    private final String logFile;
    private final String statsFile;
    private final AlignmentsStatistics statistics;
    private Parameter[] parameters;

    public BWASWTask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
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

    public AlignmentsStatistics run() {
        Timer timer = new Timer();
        BWASWAligner bwa = new BWASWAligner(forwardPath, reversePath, reference, outFile, logFile, parameters);
        this.name = bwa.getID() + "-" + name;
        timer.start();

        System.out.println("(" + new Date().toString() + ")\n" + name + ": Start running");
        Process run;
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return statistics;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.reset();
        System.out.println(name + ": Obtaining stats");
        boolean process = Sam2StatsParser.process(new File(outFile), statistics);
        timer.stop();
        if (process) System.out.println(name + ": Procesamiento terminado con éxito. " + timer.report());
        else System.err.println(name + ": Error de procesado de estadisticas");

        FileStatsWriter writer = new FileStatsWriter( statsFile);
        writer.write(parameters, statistics);
        return statistics;

    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

}
