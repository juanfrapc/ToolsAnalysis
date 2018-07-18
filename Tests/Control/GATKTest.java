package Control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class GATKTest {

    private final String reference = "Tests/tutorialFile/hg38_mini_ref/chr19_chr19_KI270866v1_alt.fasta";
    private final String marked = "Tests/tutorialFile/altalt_snaut.bam";
    private final String recaled = "Tests/tutorialFile/altalt_recal.bam";
    private final String table = "Tests/tutorialFile/altalttable.table";
    private final String bamvcf1 = "Tests/tutorialFile/HC/altalt_snaut.bam";
    private final String bamvcf2 = "Tests/tutorialFile/HC/paalt_snaut.bam";
    private final String bamvcf3 = "Tests/tutorialFile/HC/papa_snaut.bam";
    private final String gvcfComb = "Tests/tutorialFile/HC/combined.g.vcf";
    private final String gvcf1 = "Tests/tutorialFile/HC/altalt.g.vcf";
    private final String gvcf2 = "Tests/tutorialFile/HC/altpa.g.vcf";
    private final String gvcf3 = "Tests/tutorialFile/HC/papa.g.vcf";
    private final String vcf = "Tests/tutorialFile/HC/multisample.vcf";
    private final String vcfSimple = "Tests/tutorialFile/HC/simple.vcf";
    private final String vcfSimpleFiltered = "Tests/tutorialFile/HC/simpleFiltered.vcf";
    private final String snpModelRecal= "Tests/tutorialFile/HC/snp.recal";
    private final String snpModelTranches= "Tests/tutorialFile/HC/snp.tranches";
    private final String indelModelRecal= "Tests/tutorialFile/HC/indel.recal";
    private final String indelModelTranches= "Tests/tutorialFile/HC/indel.tranches";


    @BeforeEach
    void setUp() {
        File table = new File(this.table);
        if (table.delete()) System.out.println("Borrado");
        File recal = new File(this.recaled);
        if (recal.delete()) System.out.println("Borrado");
        File gvcf1 = new File(this.gvcf1);
        if (gvcf1.delete()) System.out.println("Borrado");
        File gvcf2 = new File(this.gvcf2);
        if (gvcf2.delete()) System.out.println("Borrado");
        File gvcf3 = new File(this.gvcf3);
        if (gvcf3.delete()) System.out.println("Borrado");
        File gvcfComb = new File(this.gvcfComb);
        if (gvcfComb.delete()) System.out.println("Borrado");
        File vcf = new File(this.vcf);
        if (vcf.delete()) System.out.println("Borrado");
        File simple = new File(this.vcfSimple);
        if (simple.delete()) System.out.println("Borrado");
        File filtered= new File(this.vcfSimple);
        if (filtered.delete()) System.out.println("Borrado");
        File snpRec = new File(this.snpModelRecal);
        if (snpRec.delete()) System.out.println("Borrado");
        File snpTranch = new File(this.snpModelTranches);
        if (snpTranch.delete()) System.out.println("Borrado");
        File indelRecal = new File(this.indelModelRecal);
        if (indelRecal.delete()) System.out.println("Borrado");
        File indelTranch = new File(this.indelModelTranches);
        if (indelTranch.delete()) System.out.println("Borrado");
    }

    @Test
    void baseReacalibrator() {
        try {
            Process recal = GATK.baseReacalibrator(reference, marked, table);
            int status = recal.waitFor();
            assert status == 0;
            assert new File(table).exists();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void printReads() {
        try {
            Process recal = GATK.baseReacalibrator(reference, marked, table);
            recal.waitFor();
            Process print = GATK.applyBQSR(reference, marked, recaled, table, "Tests/tutorialFile/");
            int status = print.waitFor();
            assert status == 0;
            assert new File(recaled).exists();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void haplotypeCaller() {
        try {
            Process HC = GATK.haplotypeCaller(reference, marked, gvcf1);
            int status = HC.waitFor();
            assert status == 0;
            assert new File(gvcf1).exists();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void genotypeGVCFs() {
        try {
            Process HC = GATK.haplotypeCaller(reference, marked, gvcf1);
            HC.waitFor();
            HC = GATK.haplotypeCaller(reference, marked, gvcf2);
            HC.waitFor();
            HC = GATK.haplotypeCaller(reference, marked, gvcf3);
            HC.waitFor();
            Process combine = GATK.combineGVCFs(reference, new String[]{gvcf1, gvcf2, gvcf3}, gvcfComb);
            combine.waitFor();
            Process GG = GATK.genotypeGVCFs(reference, gvcfComb, vcf);
            int status = GG.waitFor();
            assert status == 0;
            assert new File(vcf).exists();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void variantReacalibrator() {
        try {
            Process HC = GATK.haplotypeCaller(reference, marked, gvcf1);
            HC.waitFor();
            Process GG = GATK.genotypeGVCFs(reference, gvcf1, vcfSimple);
            GG.waitFor();
            Process snpModel = GATK.buildSNPModel(reference, vcfSimple, "Tests/tutorialFile/HC/"); // eliminar MQRANKSUM, READPOSRANKSUM, MQ
            snpModel.waitFor();
            assert new File(snpModelRecal).exists();
            assert new File(snpModelTranches).exists();
//            Process indelModel = GATK.buildIndelModel(reference, vcfSimple, "Tests/tutorialFile/HC/");
//            indelModel.waitFor();
//            assert new File(indelModelRecal).exists();
//            assert new File(indelModelTranches).exists();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    void printVariants() {
        try {
            Process HC = GATK.haplotypeCaller(reference, marked, gvcf1);
            HC.waitFor();
            Process GG = GATK.genotypeGVCFs(reference, gvcf1, vcfSimple);
            GG.waitFor();
            Process snpModel = GATK.buildSNPModel(reference, vcfSimple, "Tests/tutorialFile/HC/"); // eliminar MQRANKSUM, READPOSRANKSUM, MQ
            snpModel.waitFor();
            Process apply = GATK.applyVQSR(reference, vcfSimple, vcfSimpleFiltered, "Tests/tutorialFile/HC/", true);
            int status = apply.waitFor();
        } catch (Exception e) {
            assert (false);
        }
    }
}
