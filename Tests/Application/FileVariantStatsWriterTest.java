package Application;

import Control.VCF2StatsParser;
import Model.Parameter;
import Model.VariantStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

class FileVariantStatsWriterTest {

    private VariantStatistics statistics;

    @BeforeEach
    void init() {
        statistics = new VariantStatistics();
        String pathOrigin = "Tests/ejemplo.vcf";
        File file = new File(pathOrigin);
        VCF2StatsParser.process(file, statistics, true);
    }

    @Test
    void writeTest() {
        String pathSave = "Tests/ejemploVCF.stat";
        FileVariantStatsWriter writer = new FileVariantStatsWriter(pathSave);
        writer.write(new Parameter[0], statistics);
        try (Stream<String> stream = Files.lines(Paths.get(pathSave))){
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}