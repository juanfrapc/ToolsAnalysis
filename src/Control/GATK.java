package Control;

import java.io.IOException;
import java.util.List;

public class GATK {

    private static final String dbSNP = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_dbSNP.vcf.gz";
    private static String hapmap = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_hapmap.vcf.gz";
    private static String omni = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_OMNI.vcf.gz";
    private static String milG = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_1000G.vcf.gz";
    private static String mills = "/media/uichuimi/DiscoInterno/GENOME_DATA/vcfDB/gatk_resourcebundle_MILS.vcf.gz";

    public static Process baseReacalibrator(String reference, String bamInput, String tableOutput) throws IOException {
        //String finalReference = reference.split("\\.")[0] + ".2bit";
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "BaseRecalibrator",
                "-R", reference,
                "-I", bamInput,
                "-O", tableOutput,
                "--known-sites", dbSNP);
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process applyBQSR(String reference, String bamInput, String bamOutput, String table, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "ApplyBQSR",
                "-R", reference,
                "-I", bamInput,
                "-O", bamOutput,
                "-bqsr", table,
                "--TMP_DIR", path);
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process haplotypeCaller(String reference, String bamInput, String gvcfOutput, String singleName) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "HaplotypeCaller",
                "-R", reference,
                "-I", bamInput,
                "-O", gvcfOutput,
                "-ERC", "GVCF",
                "--dbsnp", dbSNP);
//                "-L", "chrX",
//                "-L", "chrY");
//        pb = pb.redirectError(new File("/home/juanfrapc/GENOME_DATA/log/MemFrequencyGeneticAlgorithm.log"));
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        if (singleName != null) {
            List<String> command = pb.command();
            command.add("-ALIAS");
            command.add(singleName);
            pb.command(command);
        }
        return pb.start();
    }

    public static Process combineGVCFs(String reference, List<String> variantsInput, String vcfOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "CombineGVCFs",
                "-R", reference,
                "-O", vcfOutput);
        List<String> command = pb.command();
        for (String variant : variantsInput) {
            command.add("-V");
            command.add(variant);
        }
        pb.command(command);
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process genotypeGVCFs(String reference, String variantInput, String vcfOutput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "GenotypeGVCFs",
                "-R", reference,
                "-V", variantInput,
                "-O", vcfOutput);
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }


    public static Process genomicsDBImport(String reference, String[] variantsInput, String dbPath) throws IOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static Process buildSNPModel(String reference, String input, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "VariantRecalibrator",
                "-R", reference,
                "-V", input,
                "--resource", "hapmap,known=false,training=true,truth=true,prior=15.0:" + hapmap,
                "--resource", "omni,known=false,training=true,truth=false,prior=12.0:" + omni,
                "--resource", "1000G,known=false,training=true,truth=false,prior=10.0:" + milG,
                "--resource", "dbsnp,known=true,training=false,truth=false,prior=2.0:" + dbSNP,
                "-an", "QD",
                "-an", "MQ", "-an", "MQRankSum","-an", "ReadPosRankSum",
                "-an", "FS", "-an", "SOR",
                "-mode", "SNP",
                "-O", path + "snp.recal",
                "--tranches-file", path + "snp.tranches"
        );
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process buildIndelModel(String reference, String input, String path) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "VariantRecalibrator",
                "-R", reference,
                "-V", input,
                "--max-gaussians", "4",
                "--resource", "mills,known=false,training=true,truth=true,prior=12.0:" + mills,
                "--resource", "dbsnp,known=true,training=false,truth=false,prior=2.0:" + dbSNP,
                "-an", "QD", "-an", "MQRankSum", "-an", "ReadPosRankSum", "-an", "FS", "-an", "SOR",
                "-mode", "INDEL",
                "-O", path + "indel.recal",
                "--tranches-file", path + "indel.tranches"
        );
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process applyVQSR(String reference, String input, String output, String path, boolean snp) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "ApplyVQSR",
                "-R", reference,
                "-V", input,
                "-O", output,
                "--truth-sensitivity-filter-level", snp ? "99.5" : "99.0",
                "--tranches-file", path + (snp ? "snp.tranches" : "indel.tranches"),
                "--recal-file", path + (snp ? "snp.recal" : "indel.recal"),
                "-mode", snp ? "SNP" : "INDEL"
        );
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process Variant2Table(String input, String output) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "-Xmx6g", "VariantsToTable",
                "-V", input,
                "-O", output,
                "-F", "CHROM", "-F", "POS", "-F", "ID", "-F", "REF", "-F", "ALT", "-F", "QUAL", "-F", "FILTER", "-F", "TYPE", "-GF", "GT"
        );
//        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }


}
