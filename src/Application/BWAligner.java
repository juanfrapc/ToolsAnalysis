package Application;

import Model.Parameter;
import Model.Aligner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BWAligner implements Aligner {


    private File forward;
    private File reverse;
    private File genome;
    private File output;
    private File log;
    private Parameter[] parameters;
    private int cores = Runtime.getRuntime().availableProcessors();

    private BWAligner(File forward, File reverse, File genome, File output, File log) {
        this.forward = forward;
        this.reverse = reverse;
        this.genome = genome;
        this.output = output;
        this.log = log;
    }

    BWAligner(String forward, String reverse, String genome, String outputPath, String logPath, Parameter[] parameters) {
        this(new File(forward), new File(reverse), new File(genome), new File(outputPath), new File(logPath));
        this.parameters = parameters;
    }

    private ProcessBuilder buildCmd() {
        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        for (Parameter parameter : this.getParameterS()) {
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
    public Process run() {
        ProcessBuilder pb = buildCmd();
        pb = pb.redirectOutput(output);
        pb = pb.redirectError(log);
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getID() {
        return "Burrows-Wheeler-Aligner";
    }

    @Override
    public Parameter[] getParameterS() {
        return parameters;
    }
}
