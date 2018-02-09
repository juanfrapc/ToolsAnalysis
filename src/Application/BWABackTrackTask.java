package Application;

import Application.Aligners.BWABackTrackAlnAligner;
import Application.Aligners.BWABackTrackSampe;
import Model.Aligner;
import Model.AlignmentsStatistics;
import Model.Parameter;
import Model.Timer;
import View.Parser.Sam2StatsParser;

import java.io.File;
import java.util.Date;

class BWABackTrackTask{

    private final String saireverse;
    private final String saiforward;
    private String name;
    private final String forwardPath;
    private final String reversePath;
    private final String reference;
    private final String outFile;
    private final String logFile;
    private final String statsFile;
    private final AlignmentsStatistics statistics;
    private final Parameter[] parameters;

    BWABackTrackTask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
        this.name = name;
        this.forwardPath = forwardPath;
        this.reversePath = reversePath;
        this.reference = reference;
        this.parameters = parameters;
        this.outFile = "/media/uichuimi/DiscoInterno/Juanfra/sams/" + name + ".sam";
        this.saiforward = "/media/uichuimi/DiscoInterno/Juanfra/sams/" + name + "forward.sai";
        this.saireverse = "/media/uichuimi/DiscoInterno/Juanfra/sams/" + name + "reverse.sam";
        this.logFile = "/media/uichuimi/DiscoInterno/Juanfra/logs/" + name + ".log";
        this.statsFile = "/media/uichuimi/DiscoInterno/Juanfra/stats/" + name + ".stat";
        this.statistics = new AlignmentsStatistics();
    }

    void run() {
        Timer timer = new Timer();
        Aligner bwa = new BWABackTrackAlnAligner(forwardPath, reference, saiforward, logFile, parameters);
        this.name = bwa.getID() + "-" + name;
        timer.start();

        System.out.println("(" + new Date().toString() + ") " + name + ": Start running forward aln");
        Process run;
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento forward terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bwa = new BWABackTrackAlnAligner(reversePath, reference, saireverse, logFile, parameters);
        this.name = bwa.getID() + "-" + name;
        timer.reset();

        System.out.println("(" + new Date().toString() + ") " + name + ": Start running reverse aln");
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento reverse terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bwa = new BWABackTrackSampe(saiforward, forwardPath, saireverse, reversePath, reference, outFile, logFile);
        this.name = bwa.getID() + "-" + name;
        timer.reset();

        System.out.println("(" + new Date().toString() + ") " + name + ": Start running sampe");
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento sampe terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return;
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

    }
}
