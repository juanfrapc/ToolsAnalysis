package Control;

import Application.Aligners.BWAMEMAligner;
import Model.Parameter;
import Model.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PreProcessor {

    private static Timer timer = new Timer();

    public static boolean getPreprocessedBamFile(String forward, String reverse, String reference, String name, String path) throws IOException, InterruptedException {
        timer.start();
        System.out.println("("+ new Date().toString() + ") Start preprocessing" );
        Parameter[] parameters = {new Parameter('a',"")};

        BWAMEMAligner bwa = new BWAMEMAligner(forward, reverse, reference, path + name+".sam", path + name + ".log", parameters);
        if(!waitforProcess(bwa.run(), "alineamiento bwa")) return false;
        Process sam2Bam = Samtools.sam2BamParallel(path + name + ".sam", path + name + ".bam");

        if(!waitforProcess(sam2Bam, "sam2bam")) return false;
        new File(path + name + ".sam").delete();

        Process sort = Samtools.sortBamParallel(path + name + ".bam", path + name + ".sorted");
        if(!waitforProcess(sort, "sort")) return false;
        new File(path + name + ".bam").delete();

        Process mark = Picard.markDuplicates(path + name + ".sorted.bam", path + name + ".sortedDeDup.bam", path + name + ".dups");
        if(!waitforProcess(mark, "mark")) return false;
        new File(path + name + ".sorted.bam").delete();

        Process recall = GATK.BaseReaclibrator(reference, path + name + ".sortedDeDup.bam", path +name + "recall_data.table");
        if(!waitforProcess(recall, "recall")) return false;

        Process applyBQSR = GATK.PrintReads(reference, path + name + ".sortedDeDup.bam", path + name + "Final.bam", path + "recall_data.table");
        if(!waitforProcess(applyBQSR, "Apply BQSR")) return false;
        new File(path + name + ".sortedDeDup.bam").delete();

        System.out.println("done");
        return true;
    }

    private static boolean waitforProcess(Process process, String name) {
        try {
            int status = process.waitFor();
            timer.stop();
            if (status==0) System.out.println(name + " terminado con exito: " + timer.report());
            else {
                System.out.println("error en " + name);
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.reset();
        return true;
    }

}
