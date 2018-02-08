package Application.Aligners;

import Model.Aligner;
import Model.Parameter;

import java.io.File;
import java.util.ArrayList;

public class BWABackTrackSampe implements Aligner{

    private File forwardSai;
    private File forwardFq;
    private File reverseSai;
    private File reverseFq;
    private File genome;
    private File output;
    private File log;
    private Parameter[] parameters;

    private BWABackTrackSampe(File forwardSai, File forwardFq, File reverseSai, File reverseFq, File genome, File output, File log) {
        this.forwardSai = forwardSai;
        this.forwardFq = forwardFq;
        this.reverseSai = reverseSai;
        this.reverseFq = reverseFq;
        this.genome = genome;
        this.output = output;
        this.log = log;
    }

    public BWABackTrackSampe(String forwardSai, String forwardFq, String reverseSai, String reverseFq, String genome, String outputPath, String logPath, Parameter[] parameters) {
        this(new File(forwardSai), new File(forwardFq), new File(reverseSai), new File(reverseFq), new File(genome), new File(outputPath), new File(logPath));
        this.parameters = parameters;
    }

    @Override
    public String getID() {
        return "Burrows-Wheeler-aligner";
    }

    @Override
    public Parameter[] getParameterS() {
        return parameters;
    }

    @Override
    public ProcessBuilder buildCmd() {
        ArrayList<String> command = new ArrayList<>();
        command.add("bwa");
        command.add("sampe");
        command.add(genome.getAbsolutePath());
        command.add(forwardSai.getAbsolutePath());
        command.add(reverseSai.getAbsolutePath());
        command.add(forwardFq.getAbsolutePath());
        command.add(reverseFq.getAbsolutePath());
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
