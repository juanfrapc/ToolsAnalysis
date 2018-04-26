package Control;

import java.io.IOException;

public class GATK {

    private static final String dbSNP= "/home/juanfrapc/GENOME_DATA/vcfDB/dbSNP_all_20180418.vcf.gz";

    public static Process BaseReaclibrator(String reference, String bamInput, String tableOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "BaseRecalibrator",
                "-R", reference,
                "-I", bamInput,
                "-O", tableOutput,
                "--known-sites", dbSNP);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process PrintReads(String reference, String bamInput, String bamOutput, String table) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "ApplyBQSRSpark",
                "-R", reference,
                "-I", bamInput,
                "-O", bamOutput,
                "-bqsr", table);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

}
