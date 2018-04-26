package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GATKTest {

    private final String reference= "/home/juanfrapc/GENOME_DATA/REFERENCE/genome.fasta";
    private final String marked = "Tests/ejemplo2Marked.bam";
    private final String table = "Tests/ejemplo2table.table";

    @BeforeEach
    void setUp() {
        File table = new File(this.table);
        if (table.delete()) System.out.println("Borrado");
    }

    @Test
    void baseReaclibrator() {
        try {
            Process recal = GATK.BaseReaclibrator(reference, marked, table);
            int status = recal.waitFor();
            assert status==0;
            assert new File(table).exists();
        } catch (Exception e) {
            assert(false);
        }


    }

}