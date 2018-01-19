package Parser;

import Model.Alignment;
import Model.AlignmentFactory;

import java.io.*;
import java.util.stream.Stream;

public class Parser {

    public Stream<Alignment> parseBam(File file) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder("samtools", "view", file.getAbsolutePath());
        final Process process = pb.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().map(AlignmentFactory::createAlignment);
    }

    public Stream<Alignment> parseSam(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.lines().map(AlignmentFactory::createAlignment);
    }

}
