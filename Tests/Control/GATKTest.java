package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class GATKTest {

    private final String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
    private final String marked= "Tests/tutorialFile/altalt_snaut.bam";
    private final String recaled= "Tests/tutorialFile/altalt_recal.bam";
    private final String table = "Tests/tutorialFile/altalttable.table";

    @BeforeEach
    void setUp() {
        File table = new File(this.table);
        if (table.delete()) System.out.println("Borrado");
        File recal = new File(this.recaled);
        if (table.delete()) System.out.println("Borrado");
    }

    @Test
    void baseReacalibrator() {
        try {
            Process recal = GATK.BaseReacalibrator(reference, marked, table);
            int status = recal.waitFor();
            assert status==0;
            assert new File(table).exists();
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    void printReads() {
        try {
            Process recal = GATK.BaseReacalibrator(reference, marked, table);
            recal.waitFor();
            Process print = GATK.PrintReads(reference, marked, recaled, table);
            int status = print.waitFor();
            assert status==0;
            assert new File(table).exists();
        } catch (Exception e) {
            assert(false);
        }
    }

}
