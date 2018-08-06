package Application.AlignningStatsTasks;

import Application.Aligners.BWABackTrackAlnAligner;
import Application.Aligners.BWABackTrackSampe;
import Application.FileAlignStatsWriter;
import Model.Aligner;
import Model.AlignmentsStatistics;
import Model.Parameter;
import Model.Timer;
import Control.Sam2StatsParser;

import java.io.File;
import java.util.Date;

public class BWABackTrackTask implements AligningTask{

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
    private Parameter[] parameters;

    public BWABackTrackTask(String name, String forwardPath, String reversePath, String reference, Parameter[] parameters) {
        this.name = name;
        this.forwardPath = forwardPath;
        this.reversePath = reversePath;
        this.reference = reference;
        this.parameters = parameters;
        this.outFile = "/home/juanfrapc/GENOME_DATA/DAM/BAM/" + name + ".sam";
        this.saiforward = "/home/juanfrapc/GENOME_DATA/DAM/BAM/" + name + "forward.sai";
        this.saireverse = "/home/juanfrapc/GENOME_DATA/DAM/BAM/" + name + "reverse.sai";
        this.logFile = "/home/juanfrapc/GENOME_DATA/DAM/log/" + name + ".log";
        this.statsFile = "/home/juanfrapc/GENOME_DATA/DAM/stats/" + name + ".stat";
        this.statistics = new AlignmentsStatistics();
    }

    public AlignmentsStatistics run() {
        Timer timer = new Timer();
        Aligner bwa = new BWABackTrackAlnAligner(forwardPath, reference, saiforward, logFile, parameters);
        this.name = name.equals("") ? bwa.getID() + "-" + name:name;
        timer.start();

        System.out.println("(" + new Date().toString() + ")\n" + name + ": Start running forward aln");
        Process run;
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento forward terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bwa = new BWABackTrackAlnAligner(reversePath, reference, saireverse, logFile, parameters);
        timer.reset();

        System.out.println("(" + new Date().toString() + ")\n" + name + ": Start running reverse aln");
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento reverse terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bwa = new BWABackTrackSampe(saiforward, forwardPath, saireverse, reversePath, reference, outFile, logFile);
        timer.reset();

        System.out.println("(" + new Date().toString() + ")\n" + name + ": Start running sampe");
        try {
            run = bwa.run();
            int error = run.waitFor();
            timer.stop();
            if (error == 0) System.out.println(name + ": alineamiento sampe terminado con éxito. " + timer.report());
            else {
                System.err.println(name + ": Error de alineamiento " + error);
                return null;
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

        FileAlignStatsWriter writer = new FileAlignStatsWriter( statsFile);
        writer.write(parameters, statistics);
        return statistics;

    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getName() {
        return "BWABackTrack";
    }
}
