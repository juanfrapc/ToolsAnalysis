package Control;

import Model.Timer;

import java.io.IOException;
import java.util.Date;

public class GermlineSNP {

    private static Timer timer = new Timer();

    public static boolean getVCF(String reference, String name, String bamPath, String vcfPath) throws IOException {
        timer.start();
        System.out.println("(" + new Date().toString() + ") Start getting VCF");

        Process haplotypeCaller = GATK.HaplotypeCaller(reference, bamPath + name + "Final.bam", vcfPath + name + ".vcf");
        if (!waitforProcess(haplotypeCaller, "Haplotype Caller")) return false;

        Process genotype = GATK.GenotypeGVCFs(reference, vcfPath + name + ".g.vcf", vcfPath + name + ".vcf");
        return waitforProcess(haplotypeCaller, "Genotype GVCF");
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
