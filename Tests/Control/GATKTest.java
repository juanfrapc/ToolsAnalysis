package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class GATKTest {

    private final String reference= "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
    private final String marked= "Tests/tutorialFile/altalt_snaut.bam";
    private final String recaled= "Tests/tutorialFile/altalt_recal.bam";
    private final String table = "Tests/tutorialFile/altalttable.table";
    private final String bamvcf1= "Tests/tutorialFile/HC/altalt_snaut.bam";
    private final String bamvcf2= "Tests/tutorialFile/HC/paalt_snaut.bam";
    private final String bamvcf3= "Tests/tutorialFile/HC/papa_snaut.bam";
    private final String gvcf= "Tests/tutorialFile/HC/combined.g.vcf";
    private final String gvcf1= "Tests/tutorialFile/HC/altalt.g.vcf";
    private final String gvcf2= "Tests/tutorialFile/HC/altpa.g.vcf";
    private final String gvcf3= "Tests/tutorialFile/HC/papa.g.vcf";
    private final String vcf = "Tests/tutorialFile/HC/multisample.vcf";

    @BeforeEach
    void setUp() {
        File table = new File(this.table);
        if (table.delete()) System.out.println("Borrado");
        File recal = new File(this.recaled);
        if (table.delete()) System.out.println("Borrado");
//        File gvcf= new File(this.gvcf1);
//        if (gvcf.delete()) System.out.println("Borrado");
//        File gvcf2= new File(this.gvcf2);
//        if (gvcf.delete()) System.out.println("Borrado");
//        File gvcf3= new File(this.gvcf3);
//        if (gvcf.delete()) System.out.println("Borrado");
        File gvcf= new File(this.gvcf);
        if (gvcf.delete()) System.out.println("Borrado");
        File vcf= new File(this.vcf);
        if (vcf.delete()) System.out.println("Borrado");
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
            Process print = GATK.PrintReads(reference, marked, recaled, table, "Tests/tutorialFile/");
            int status = print.waitFor();
            assert status==0;
            assert new File(table).exists();
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    void haplotypeCaller() {
        try {
            Process HC = GATK.HaplotypeCaller(reference, marked, gvcf1);
            int status = HC.waitFor();
            assert status==0;
            assert new File(gvcf1).exists();
        } catch (Exception e) {
            assert(false);
        }
    }

    @Test
    void genotypeGVCFs() {
        try {
//            Process HC = GATK.HaplotypeCaller(reference, marked, gvcf1);
//            HC.waitFor();
//            HC = GATK.HaplotypeCaller(reference, marked, gvcf2);
//            HC.waitFor();
//            HC = GATK.HaplotypeCaller(reference, marked, gvcf3);
//            HC.waitFor();
            Process combine = GATK.CombineGVCFs(reference, new String[]{gvcf1,gvcf2, gvcf3}, gvcf);
            combine.waitFor();
            Process GG = GATK.GenotypeGVCFs(reference, gvcf, vcf);
            int status = GG.waitFor();
            assert status==0;
            assert new File(vcf).exists();
        } catch (Exception e) {
            assert(false);
        }
    }
}
