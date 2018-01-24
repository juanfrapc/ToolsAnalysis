package Application;

import Model.Parameter;
import Model.Aligner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BWAligner implements Aligner {


    private File forward;
    private File reverse;
    private File genome;
    private File output;
    private Parameter[] parameters;
    private int cores = Runtime.getRuntime().availableProcessors();

    private BWAligner(File forward, File reverse, File genome, File output) {
        this.forward = forward;
        this.reverse = reverse;
        this.genome = genome;
        this.output = output;
    }

    BWAligner(String forward, String reverse, String genome, String outputPath, Parameter[] parameters) {
        this(new File(forward), new File(reverse), new File(genome), new File(outputPath));
        this.parameters = parameters;
    }

    private ProcessBuilder buildCmd() {
        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        for (Parameter parameter : parameters) {
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
    public Process run() throws IOException {
        ProcessBuilder pb = buildCmd();
        pb.redirectOutput(output);
        return pb.start();
    }

    @Override
    public String getID() {
        return "Burrows-Wheeler Aligner";
    }

    @Override
    public Parameter[] getParameter() {
        return parameters;
    }
}
