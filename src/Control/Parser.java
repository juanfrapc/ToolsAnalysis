package Control;

import Model.Alignment;
import Model.AlignmentFactory;

import java.io.*;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.US_ASCII;

class Parser {

    static String samtools = "/home/juanfrapc/Público/samtools/bin/samtools";

    Stream<Alignment> parseBam(File file) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(samtools, "view", file.getAbsolutePath());
        final Process process = pb.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines().map(AlignmentFactory::createAlignment);
    }

    Stream<Alignment> parseSam(File file) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), US_ASCII));
        //BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.lines().filter(alignment -> alignment!= null).map(AlignmentFactory::createAlignment);
    }

}
