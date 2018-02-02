package Application;

import Model.AlignmentsStatistics;
import Parser.ParseFileStats;
import View.ConsoleStatsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ConsoleStatsWriterTest {

    private AlignmentsStatistics statistics;
    private String path = "Tests/ejemplo.sam";

    @BeforeEach
    void init() throws IOException {
        statistics = new AlignmentsStatistics();
        File file = new File(path);
        ParseFileStats.process(file, statistics);
    }

    @Test
    void writeTest() {
        ConsoleStatsWriter writer = new ConsoleStatsWriter();
        writer.write(statistics);
    }

}