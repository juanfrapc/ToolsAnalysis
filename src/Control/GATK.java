package Control;

import java.io.IOException;

public class GATK {

    static int cores = Runtime.getRuntime().availableProcessors();

    public static Process BaseReaclibrator(String reference, String bamInput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("gatk", "--java-options", "Xmx6g", "BaseRecalibrator",
                "-R", "reference",
                "-I", bamInput);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

}
