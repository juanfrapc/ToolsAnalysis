package Control;


import java.io.IOException;

public class Sam2Bam {

    public static Process convert(String sam, String bam) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(Parser.samtools, "view", "-S", "-h", "-b", sam, "-o", bam);
        return pb.start();
    }

    public static Process convertParallel(String sam, String bam) throws IOException, InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        ProcessBuilder pb = new ProcessBuilder(Parser.samtools, "view", "-S", "-h", "-b", "-@", ""+cores,
                sam,
                "-o", bam);
        return pb.start();
    }

}
