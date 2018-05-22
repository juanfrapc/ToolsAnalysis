package Control;

import java.io.IOException;

public class GATK {

    private static final String dbSNP= "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_dbSNP.vcf.gz";

    public static Process BaseReacalibrator(String reference, String bamInput, String tableOutput) throws IOException {
        //String finalReference = reference.split("\\.")[0] + ".2bit";
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "BaseRecalibrator",
                "-R", reference,
                "-I", bamInput,
                "-O", tableOutput,
                "--known-sites", dbSNP);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process PrintReads(String reference, String bamInput, String bamOutput, String table, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "ApplyBQSR",
                "-R", reference,
                "-I", bamInput,
                "-O", bamOutput,
                "-bqsr", table,
                "--TMP_DIR", path);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process HaplotypeCaller(String reference, String bamInput, String gvcfOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "HaplotypeCaller",
                "-R", reference,
                "-I", bamInput,
                "-O", gvcfOutput,
                "-ERC", "GVCF");
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process GenotypeGVCFs(String reference, String[] variantsInput, String vcfOutput) throws IOException {
        String variants="";
        for (String variant : variantsInput) {
            variants += "-V" + variant;
        }
        System.out.println(variants);
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "GenotypeGVCFs",
                "-R", reference,
                variants,
                "-O", vcfOutput);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }



}
