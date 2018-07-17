package Control;

import java.io.IOException;
import java.util.List;

public class GATK {

    private static final String dbSNP = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_dbSNP.vcf.gz";
    private static String hapmap;
    private static String omni;
    private static String milG;
    private static String mills;

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

    public static Process combineGVCFs(String reference, String[] variantsInput, String vcfOutput) throws IOException {
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

    public static Process buildSNPModel(String reference, String input, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "VariantRecalibrator",
                "-R", reference,
                "-V", input,
                "--resource", "hapmap,known=false,training=true,truth=true,prior=15.0:" + hapmap,
                "--resource", "omni,known=false,training=true,truth=false,prior=12.0:" + omni,
                "--resource", "1000G,known=false,training=true,truth=false,prior=10.0:" + milG,
                "--resource", "dbsnp,known=true,training=false,truth=false,prior=2.0:" + dbSNP,
                "-an ", "QD", "-an", "MQ", "-an", "MQRankSum", "-an", "ReadPosRankSum", "-an", "FS", "-an", "SOR",
                "-mode", "SNP",
                "--recal-file", path + "snp.recal",
                "--tranches-file", path + "output_snp.tranches",
                "--rscript-file", path + "output_snp.plots.R"
        );
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process buildIndelModel(String reference, String input, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "VariantRecalibrator",
                "-R", reference,
                "-V", input,
                "--max-gaussians", "4",
                "--resource", "mills,known=false,training=true,truth=true,prior=12.0:" + mills,
                "--resource", "dbsnp,known=true,training=false,truth=false,prior=2.0:" + dbSNP,
                "-an ", "QD", "-an", "MQRankSum", "-an", "ReadPosRankSum", "-an", "FS", "-an", "SOR",
                "-mode", "INDEL",
                "--recal-file", path + "indel.recal",
                "--tranches-file", path + "output_indel.tranches",
                "--rscript-file", path + "output_indel.plots.R"
        );
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process applyVQSR(String reference, String input, String output, String path, boolean snp) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "ApplyVQSR",
                "-R", reference,
                "-V", input,
                "-O", output,
                "--truth-sensitivity-filter-level", snp?"99.5":"99.0",
                "--tranches-file", snp? "output_snp.tranches":"output_indel.tranches",
                "--recal-file", snp? "snp.recal":"indel.recal",
                "-mode", snp?"SNP":"INDEL"
        );
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }


}
