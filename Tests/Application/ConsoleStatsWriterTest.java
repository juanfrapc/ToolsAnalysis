package Application;

import Model.AlignmentStatistics;
import Parser.ProcessFileStats;
import View.ConsoleStatsWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ConsoleStatsWriterTest {

    private AlignmentStatistics statistics;
    private String path = "ejemplo.sam";

    @BeforeEach
    void init() throws IOException {
        statistics = new AlignmentStatistics();
        File file = new File(path);
        ProcessFileStats.process(file, statistics);
    }

    @Test
    void writeTest() {
        ConsoleStatsWriter writer = new ConsoleStatsWriter();
        writer.write(statistics);
    }

}