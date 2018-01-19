package Application;

import Model.Alignment;
import Model.AlignmentStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ProcessFileStatsTest {

    private File file;
    private AlignmentStatistics statistics;

    @BeforeEach
    void setUp() {
        file = new File("ejemplo.sam");
        statistics = new AlignmentStatistics();
        ProcessFileStats.process(file, statistics);
    }

    @Test
    void processSam() {
        assertEquals(10, statistics.getTotal(), "Error in total count");
        assertEquals(131, statistics.getTotalMapQ(), "Error in total Mapq");
        assertEquals(4, statistics.getUnmapped(), "Error in unmapped count");
        assertEquals(0, statistics.getUnmappedMapQ(), "Error in unMapped Mapq");
    }

}