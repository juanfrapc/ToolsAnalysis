package Application.Aligners;

import Model.Aligner;
import Model.Parameter;

import java.io.File;
import java.util.ArrayList;

public class BWABackTrackSampe implements Aligner{

    private final File forwardSai;
    private final File forwardFq;
    private final File reverseSai;
    private final File reverseFq;
    private final File genome;
    private final File output;
    private final File log;
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

    public BWABackTrackSampe(String forwardSai, String forwardFq, String reverseSai, String reverseFq, String genome, String outputPath, String logPath) {
        this(new File(forwardSai), new File(forwardFq), new File(reverseSai), new File(reverseFq), new File(genome), new File(outputPath), new File(logPath));
    }

    @Override
    public String getID() {
        return "Burrows-Wheeler-Aligner-Sampe";
    }

    @Override
    public Parameter[] getParameters() {
        return parameters;
    }

    @Override
    public ProcessBuilder buildAlignerCmd() {
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
