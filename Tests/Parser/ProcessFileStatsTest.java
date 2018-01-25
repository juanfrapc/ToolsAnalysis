package Parser;

import Model.AlignmentsStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ProcessFileStatsTest {

    private File file;
    private AlignmentsStatistics statistics;

    @BeforeEach
    void setUp() {
        statistics = new AlignmentsStatistics();
    }

    @Test
    void processSam() {
        file = new File("Tests/ejemplo.sam");
        ProcessFileStats.process(file, statistics);
        assertEquals(131, statistics.getTotalMapQ(), "Error in total MAPQ");
        assertEquals(4, statistics.getUnmapped(), "Error in unmapped count");
        assertEquals(0, statistics.getUnmappedMapQ(), "Error in unmapped MAPQ");
        assertEquals(1, statistics.getMultiplyMapped(), "Error in multiply mapped count");
        assertEquals(33, statistics.getMultiplyMapQ(), "Error in multiply mapped Mapq");
    }

    @Test
    void processBam(){
        file = new File("Tests/ejemplo.bam");
        ProcessFileStats.process(file, statistics);
        assertEquals(10, statistics.getTotal(), "Error in total count");
        assertEquals(0, statistics.getUnmapped(), "Error in unmapped count");
        assertEquals(0, statistics.getUnmappedMapQ(), "Error in unmapped MAPQ");
        assertEquals(1, statistics.getMultiplyMapped(), "Error in multiply mapped count");
        assertEquals(33, statistics.getMultiplyMapQ(), "Error in multiply mapped Mapq");
    }

}