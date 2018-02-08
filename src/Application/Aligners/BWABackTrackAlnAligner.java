package Application.Aligners;

import Model.Aligner;
import Model.Parameter;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class BWABackTrackAlnAligner implements Aligner{

    private File forward;
    private File genome;
    private File output;
    private File log;
    private Parameter[] parameters;
    private int cores = Runtime.getRuntime().availableProcessors();

    private BWABackTrackAlnAligner(File forward, File genome, File output, File log) {
        this.forward = forward;
        this.genome = genome;
        this.output = output;
        this.log = log;
    }

    public BWABackTrackAlnAligner(String forward, String genome, String outputPath, String logPath, Parameter[] parameters) {
        this(new File(forward), new File(genome), new File(outputPath), new File(logPath));
        this.parameters = parameters;
    }

    @Override
    public Parameter[] getParameterS() {
        return parameters;
    }

    @Override
    public ProcessBuilder buildCmd() {
        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        command.add("aln");
        for (Parameter parameter : this.getParameterS()) {
            if (parameter.getName() != 0) command.add("-" + parameter.getName());
            if (!parameter.getValue().equals("")) command.add(parameter.getValue());
        }
        command.add("-t");
        command.add("" + cores);
        command.add(genome.getAbsolutePath());
        command.add(forward.getAbsolutePath());
        return new ProcessBuilder(command);
    }

    @Override
    public File getOutput() {
        return output;
    }

    @Override
    public File getLog() {
        return log;
    }

    @Override
    public String getID() {
        return "Burrows-Wheeler-Aligner";
    }
}
