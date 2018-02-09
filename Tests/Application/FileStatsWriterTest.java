package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;
import View.Parser.Sam2StatsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class FileStatsWriterTest {

    private AlignmentsStatistics statistics;
    private String pathOrigin = "Tests/ejemplo.sam";
    private String pathSave = "Tests/ejemplo.stat";

    @BeforeEach
    void init() throws IOException {
        statistics = new AlignmentsStatistics();
        File file = new File(pathOrigin);
        Sam2StatsParser.process(file, statistics);
    }


    @Test
    void writeTest() {
        FileStatsWriter writer = new FileStatsWriter(pathSave);
        writer.write(new Parameter[0], statistics);
        try (Stream<String> stream = Files.lines(Paths.get(pathSave))){
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}