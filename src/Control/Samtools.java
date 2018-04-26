package Control;

import java.io.IOException;

public class Samtools {

    static int cores = Runtime.getRuntime().availableProcessors();

    public static Process sam2Bam(String sam, String bam) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("samtools", "view", "-S", "-h", "-b", sam, "-o", bam);
        return pb.start();
    }

    public static Process sam2BamParallel(String sam, String bam) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("samtools1.8", "view", "-S", "-h", "-b", "-@", ""+cores,
                sam,
                "-o", bam);
        return pb.start();
    }

    public static Process sortBam(String unsorted, String sortedPrefix) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("samtools", "sort", "-m", "4G",
                unsorted,
                sortedPrefix);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process sortBamParallel(String unsorted, String sortedPreffix) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("samtools1.8", "sort",
                "-o", sortedPreffix, "-O", "bam",
                "-T", "temp",
                "-@", ""+ cores,
                unsorted);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process index(String file) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("samtools1.8", "index", file);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

}
