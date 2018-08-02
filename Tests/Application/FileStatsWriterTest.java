package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;
import Control.Sam2StatsParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class FileStatsWriterTest {

    private AlignmentsStatistics statistics;

    @BeforeEach
    void init() {
        statistics = new AlignmentsStatistics();
        String pathOrigin = "Tests/ejemplo.sam";
        File file = new File(pathOrigin);
        Sam2StatsParser.process(file, statistics);
    }


    @Test
    void writeTest() {
        String pathSave = "Tests/ejemplo.stat";
        FileAlignStatsWriter writer = new FileAlignStatsWriter(pathSave);
        writer.write(new Parameter[0], statistics);
        try (Stream<String> stream = Files.lines(Paths.get(pathSave))){
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}