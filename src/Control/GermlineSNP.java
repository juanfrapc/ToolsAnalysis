package Control;

import Model.Timer;

import java.io.IOException;
import java.util.Date;

public class GermlineSNP {

    private static Timer timer = new Timer();

    public static boolean getVCFilered(String reference, String name, String bamPath, String vcfPath) throws IOException {
        timer.start();
        System.out.println("(" + new Date().toString() + ") Start getting VCF");

        Process haplotypeCaller = GATK.haplotypeCaller(reference, bamPath + name + ".bam", vcfPath + name + ".g.vcf");
        if (!waitforProcess(haplotypeCaller, "Haplotype Caller")) return false;

        Process genotype = GATK.genotypeGVCFs(reference, vcfPath + name + ".g.vcf", vcfPath + name + "_raw.vcf");
        waitforProcess(haplotypeCaller, "Genotype GVCF");

        Process modelSNP = GATK.buildSNPModel(reference, vcfPath + name + "_raw.vcf", vcfPath);
        waitforProcess(modelSNP, "SNP filter model");
        Process filterSNP = GATK.applyVQSR(reference, vcfPath + name + "_raw.vcf",vcfPath + name + "_SNPRecal.vcf", vcfPath, true);
        waitforProcess(filterSNP, "SNP filter applyy");

        Process modelindel = GATK.buildIndelModel(reference, vcfPath + name + "_SNPRecal.vcf", vcfPath);
        waitforProcess(modelSNP, "SNP filter model");
        Process filterIndel = GATK.applyVQSR(reference, vcfPath + name + "_SNPRecal.vcf",vcfPath + name + "_FullRecal.vcf", vcfPath, false);
        return waitforProcess(filterSNP, "SNP filter applyy");
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
