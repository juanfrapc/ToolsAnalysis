package Parser;

import Model.AlignmentsStatistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessFileStatsTest {

    private File file;
    private AlignmentsStatistics statistics;
    private Map<Integer, Long> totalMapQDistribution = new HashMap<>();
    private Map<Integer, Long> unmappedMapQDistribution = new HashMap<>();
    private Map<Integer, Long> multiplyMapQDistribution = new HashMap<>();
    private Map<Integer, Long> uniquelyMapQDistribution= new HashMap<>();

    @BeforeEach
    void setUp() {
        statistics = new AlignmentsStatistics();
        totalMapQDistribution.put(0, (long) 6);
        totalMapQDistribution.put(12, (long) 1);
        totalMapQDistribution.put(33, (long) 1);
        totalMapQDistribution.put(40, (long) 1);
        totalMapQDistribution.put(46, (long) 1);

        unmappedMapQDistribution.put(0, (long) 4);
        multiplyMapQDistribution.put(33, (long) 1);
        uniquelyMapQDistribution.put(0, (long) 2);
        uniquelyMapQDistribution.put(12, (long) 1);
        uniquelyMapQDistribution.put(40, (long) 1);
        uniquelyMapQDistribution.put(46, (long) 1);
    }

    @Test
    void processSamFreq() {
        file = new File("Tests/ejemplo.sam");
        ProcessFileStats.process(file, statistics);
        assertEquals(10, statistics.getTotal(), "Error in total count");
        assertEquals(131, statistics.getTotalMapQ(), "Error in total MAPQ");
        assertEquals(4, statistics.getUnmapped(), "Error in unmapped count");
        assertEquals(0, statistics.getUnmappedMapQ(), "Error in unmapped MAPQ");
        assertEquals(1, statistics.getMultiplyMapped(), "Error in multiply mapped count");
        assertEquals(33, statistics.getMultiplyMapQ(), "Error in multiply mapped MAPQ");
        assertEquals(5, statistics.getUniquelyMapped(), "Error in uniquely mapped count");
        assertEquals(98, statistics.getUniquelyMapQ(), "Error in uniquely mapped MAPQ");
    }

    @Test
    void processSamDistribution(){
        file = new File("Tests/ejemplo.sam");
        ProcessFileStats.process(file, statistics);
        assertEquals(totalMapQDistribution, statistics.getTotalMapQDistribution(), "Error in total MapQ distribution");
        assertEquals(unmappedMapQDistribution, statistics.getUnmappedMapQDistribution(), "Error in Unmapped MapQ distribution");
        assertEquals(multiplyMapQDistribution, statistics.getMultiplyMapQDistribution(), "Error in Multiply MapQ distribution");
        assertEquals(uniquelyMapQDistribution, statistics.getUniquelyMapQDistribution(), "Error in Uniquely MapQ distribution");
    }

    @Test
    void processBamFreq(){
        file = new File("Tests/ejemplo.bam");
        ProcessFileStats.process(file, statistics);
        assertEquals(10, statistics.getTotal(), "Error in total count");
        assertEquals(131, statistics.getTotalMapQ(), "Error in total MAPQ");
        assertEquals(0, statistics.getUnmapped(), "Error in unmapped count");
        assertEquals(0, statistics.getUnmappedMapQ(), "Error in unmapped MAPQ");
        assertEquals(1, statistics.getMultiplyMapped(), "Error in multiply mapped count");
        assertEquals(33, statistics.getMultiplyMapQ(), "Error in multiply mapped Mapq");
        assertEquals(9, statistics.getUniquelyMapped(), "Error in uniquely mapped count");
        assertEquals(98, statistics.getUniquelyMapQ(), "Error in uniquely mapped MAPQ");
    }

    @Test
    void processBamDistribution(){
        file = new File("Tests/ejemplo.bam");
        ProcessFileStats.process(file, statistics);
        assertEquals(totalMapQDistribution, statistics.getTotalMapQDistribution(), "Error in total MapQ distribution");
        assertEquals(new HashMap<>(), statistics.getUnmappedMapQDistribution(), "Error in Unmapped MapQ distribution");
        assertEquals(multiplyMapQDistribution, statistics.getMultiplyMapQDistribution(), "Error in Multiply MapQ distribution");
        uniquelyMapQDistribution.put(0, 6L);
        assertEquals(uniquelyMapQDistribution, statistics.getUniquelyMapQDistribution(), "Error in Uniquely MapQ distribution");

    }

}
