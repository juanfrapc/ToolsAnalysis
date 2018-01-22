package Application;

import Model.AlignmentStatistics;
import Parser.ProcessFileStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class FileStatsWriterTest {

    private AlignmentStatistics statistics;
    private String pathOrigin = "ejemplo.sam";
    private String pathSave = "ejemplo.stat";

    @BeforeEach
    void init() throws IOException {
        statistics = new AlignmentStatistics();
        File file = new File(pathOrigin);
        ProcessFileStats.process(file, statistics);
    }


    @Test
    void writeTest() {
        FileStatsWriter writer = new FileStatsWriter(pathSave);
        writer.write(statistics);
        try (Stream<String> stream = Files.lines(Paths.get(pathSave))){
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}