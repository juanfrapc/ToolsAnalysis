package Control;

import java.io.IOException;

public class Picard {

    public static String picard = "/home/juanfrapc/Público/picard.jar";

    public static Process markDuplicates (String unmarked, String marked, String metrics) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java","-Xmx6g", "-jar", picard,"MarkDuplicates",
                "INPUT="+ unmarked,
                "OUTPUT="+ marked,
                "MAX_RECORDS_IN_RAM=2000000",
                "VALIDATION_STRINGENCY=SILENT",
                "ASSUME_SORT_ORDER=coordinate",
                "METRICS_FILE="+metrics);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

}
