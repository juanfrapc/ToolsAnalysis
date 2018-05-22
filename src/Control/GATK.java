package Control;

import java.io.IOException;
import java.util.List;

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

    public static Process GenotypeGVCFs(String reference, String variantInput, String vcfOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "GenotypeGVCFs",
                "-R", reference,
                "-V", variantInput,
                "-O", vcfOutput);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process CombineGVCFs(String reference, String[] variantsInput, String vcfOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "CombineGVCFs",
                "-R", reference,
                "-O", vcfOutput);
        List<String> command = pb.command();
        for (String variant : variantsInput) {
            command.add("-V");
            command.add(variant);
        }
        pb.command(command);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }



}
