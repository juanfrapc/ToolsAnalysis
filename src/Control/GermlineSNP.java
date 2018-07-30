package Control;

import Model.Timer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class GermlineSNP {

    private static Timer timer = new Timer();

    public static boolean getVCFilteredFromSingelBAM(String reference, String name, String bamPath, String vcfPath) throws IOException {
        timer.start();
        System.out.println("*********************\n(" + new Date().toString() + ") Start getting VCF");

        Process haplotypeCaller = GATK.haplotypeCaller(reference, bamPath + name + ".bam", vcfPath + name + ".g.vcf", null);
        if (!waitforProcess(haplotypeCaller, "Haplotype Caller")) return false;

        Process genotype = GATK.genotypeGVCFs(reference, vcfPath + name + ".g.vcf", vcfPath + name + "_raw.vcf");
        waitforProcess(genotype, "Genotype GVCF");

        Process modelSNP = GATK.buildSNPModel(reference, vcfPath + name + "_raw.vcf", vcfPath);
        waitforProcess(modelSNP, "SNP filter model");
        Process filterSNP = GATK.applyVQSR(reference, vcfPath + name + "_raw.vcf", vcfPath + name + "_SNPRecal.vcf", vcfPath, true);
        waitforProcess(filterSNP, "SNP filter apply");

        Process modelindel = GATK.buildIndelModel(reference, vcfPath + name + "_SNPRecal.vcf", vcfPath);
        waitforProcess(modelindel, "SNP filter model");
        Process filterIndel = GATK.applyVQSR(reference, vcfPath + name + "_SNPRecal.vcf", vcfPath + name + "_FullRecal.vcf", vcfPath, false);
        return waitforProcess(filterIndel, "SNP filter apply");
    }

    public static boolean getVCFilteredFromMultiBAM(String reference, String name, String bamPath, String vcfPath,
                                                    String sampleA, String sampleB) throws IOException {
        timer.start();
        System.out.println("*********************\n(" + new Date().toString() + ") Start getting VCF");

        String outAname = sampleA + "_" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "_" + "GATK_GRCH38";
        String outBname = sampleB + "_" + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "_" + "GATK_GRCH38";
        Process haplotypeCallerSampleA = GATK.haplotypeCaller(reference, bamPath + name + ".bam", vcfPath + outAname + ".g.vcf", sampleA);
        if (!waitforProcess(haplotypeCallerSampleA, "Haplotype Caller sample A")) return false;
        Process haplotypeCallerSampleB = GATK.haplotypeCaller(reference, bamPath + name + ".bam", vcfPath + outBname + ".g.vcf", sampleB);
        if (!waitforProcess(haplotypeCallerSampleB, "Haplotype Caller sample B")) return false;

        ArrayList<String> variants = new ArrayList<>();
        variants.add(vcfPath + outAname + ".g.vcf");
        variants.add(vcfPath + outBname + ".g.vcf");
        Process combination = GATK.combineGVCFs(reference, variants, vcfPath + name + "_combined.g.vcf");
        waitforProcess(combination, "Combining GVCF");
        Process genotype = GATK.genotypeGVCFs(reference, vcfPath + name + "_combined.g.vcf", vcfPath + name + "_raw.vcf");
        waitforProcess(genotype, "Genotype GVCF");

        Process modelSNP = GATK.buildSNPModel(reference, vcfPath + name + "_raw.vcf", vcfPath);
        waitforProcess(modelSNP, "SNP filter model");
        Process filterSNP = GATK.applyVQSR(reference, vcfPath + name + "_raw.vcf", vcfPath + name + "_SNPRecal.vcf", vcfPath, true);
        waitforProcess(filterSNP, "SNP filter apply");

        Process modelindel = GATK.buildIndelModel(reference, vcfPath + name + "_SNPRecal.vcf", vcfPath);
        waitforProcess(modelindel, "Indel filter model");
        Process filterIndel = GATK.applyVQSR(reference, vcfPath + name + "_SNPRecal.vcf", vcfPath + name + "_FullRecal.vcf", vcfPath, false);
        return waitforProcess(filterIndel, "Indel filter apply");
    }

    private static boolean waitforProcess(Process process, String name) {
        try {
            int status = process.waitFor();
            timer.stop();
            if (status == 0) System.out.println(name + " terminado con exito: " + timer.report());
            else {
                System.out.println("error en " + name);
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.reset();
        return true;
    }

}
