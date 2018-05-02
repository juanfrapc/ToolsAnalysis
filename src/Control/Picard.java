package Control;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Picard {

    public static String picard = "/home/juanfrapc/Público/picard.jar";

    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

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

    public static Process fastQ2Sam (String forward, String reverse, String outputbam, String readgroup, String name) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java","-Xmx6g", "-jar", picard,"FastqToSam",
                "FASTQ="+ forward,
                "FASTQ2="+ reverse,
                "OUTPUT="+ outputbam,
                "READ_GROUP_NAME="+ readgroup,
                "SAMPLE_NAME="+ name,
                "LIBRARY_NAME=Solexa-272222",
                "PLATFORM=illumina");
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process markIlluminaAdapters(String input, String output, String metrics, String tmp) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java","-Xmx6g", "-jar", picard,"MarkIlluminaAdapters",
                "I=" + input,
 		    	"O=" + output,
	        	"M=" + metrics,
    	    	"TMP_DIR=" + tmp);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process sam2fastq(String input, String output, String tmp) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java","-Xmx6g", "-jar", picard,"SamToFastq",
                "I=" + input,
 		    	"FASTQ=" + output,
                "CLIPPING_ATTRIBUTE=XT",
                "CLIPPING_ACTION=2",
                "INTERLEAVE=true",
                "NON_PF=true",
    	    	"TMP_DIR=" + tmp);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }

    public static Process MergeBamAlignment(String inputUnmapped, String inputMapped, String output, String reference, String tmp) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java","-Xmx6g", "-jar", picard,"MergeBamAlignment",
                "R=" + reference,
                "UNMAPPED_BAM=" + inputUnmapped,
                "ALIGNED_BAM=" + inputMapped,
 		    	"O=" + output,
                "CREATE_INDEX=true",
                "CLIP_ADAPTERS=false",
                "MAX_INSERTIONS_OR_DELETIONS=-1",
                "PRIMARY_ALIGNMENT_STRATEGY=MostDistant",
                "ATTRIBUTES_TO_RETAIN=XS",
    	    	"TMP_DIR=" + tmp);
        pb = pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        return pb.start();
    }
}
