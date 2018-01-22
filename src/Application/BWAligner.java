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
    private Parameter[] parameters;
    private int cores = Runtime.getRuntime().availableProcessors();

    private BWAligner(File forward, File reverse, File genome) {
        this.forward = forward;
        this.reverse = reverse;
        this.genome = genome;
    }

    BWAligner(String forward, String reverse, String genome, Parameter[] parameters) {
        this(new File(forward), new File(reverse), new File(genome));
        this.parameters = parameters;
    }

    private ProcessBuilder buildCmd() {

        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        for(Parameter parameter:parameters){
            command.add(parameter.toString());
        }
        command.add("-t " + cores + " ");
        command.add(genome.getAbsolutePath());
        command.add(forward.getAbsolutePath());
        command.add(reverse.getAbsolutePath());
        return new ProcessBuilder(command.toArray(new String[0]));
    }


    @Override
    public Process run() throws IOException {
        ProcessBuilder pb = buildCmd();
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
