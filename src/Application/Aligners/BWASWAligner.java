package Application.Aligners;

import Model.Aligner;
import Model.Parameter;

import java.io.File;
import java.util.ArrayList;

public class BWASWAligner implements Aligner{

    private final File forward;
    private final File reverse;
    private final File genome;
    private final File output;
    private final File log;
    private Parameter[] parameters;
    private final int cores = Runtime.getRuntime().availableProcessors();

    private BWASWAligner(File forward, File reverse, File genome, File output, File log) {
        this.forward = forward;
        this.reverse = reverse;
        this.genome = genome;
        this.output = output;
        this.log = log;
    }

    public BWASWAligner(String forward, String reverse, String genome, String outputPath, String logPath, Parameter[] parameters) {
        this(new File(forward), new File(reverse), new File(genome), new File(outputPath), new File(logPath));
        this.parameters = parameters;
    }


    @Override
    public String getID() {
        return "Burrows-Wheeler-Aligner-SW";
    }

    @Override
    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public ProcessBuilder buildAlignerCmd() {
        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        command.add("bwasw");
        for (Parameter parameter : this.getParameters()) {
            if (parameter.getName() != 0) command.add("-" + parameter.getName());
            if (!parameter.getValue().equals("")) command.add(parameter.getValue());
        }
        command.add("-t");
        command.add("" + cores);
        command.add(genome.getAbsolutePath());
        command.add(forward.getAbsolutePath());
        command.add(reverse.getAbsolutePath());
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
}
