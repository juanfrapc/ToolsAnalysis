package Control;

import Application.Aligners.BWAMEMAligner;
import Model.Parameter;
import Model.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PreProcessor {

    private static Timer timer = new Timer();

    public static boolean getInterleavedFastq(String forward, String reverse, String name, String path, String readGroup) throws IOException {
        timer.start();
        System.out.println("(" + new Date().toString() + ") Start getting interleaved");

        Process fastq2sam = Picard.fastQ2Sam(forward, reverse, path + name + ".ubam", readGroup, name);
        if (!waitforProcess(fastq2sam, "fastq2sam")) return false;

        Process markIll = Picard.markIlluminaAdapters(path + name + ".ubam", path + name + "marked.bam", path + name + "metric.txt");
        if (!waitforProcess(markIll, "marking illumina")) return false;

        Process sam2Fastq = Picard.sam2fastq(path + name + "marked.bam", path + name + "_interleaved.fq");
        if (!waitforProcess(sam2Fastq, "sam2fastq")) return false;
        new File(path + name + "marked.bam").delete();

        try {
            System.out.println("compressing fastaq");
            Runtime.getRuntime().exec("gzip -f " + path + name + "_interleaved.fq").waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean getPreprocessedFromInterleaved(String bamUnmapped, String fastq, String reference, String name, String path) throws IOException, InterruptedException {
        timer.start();

        System.out.println("(" + new Date().toString() + ") Start getting Bam from interleaved");
        Parameter[] parameters = {new Parameter('p', "")};

        BWAMEMAligner bwa = new BWAMEMAligner(fastq, "", reference, path + name + ".sam", path + name + ".log", parameters);
        if (!waitforProcess(bwa.run(), "alineamiento bwa")) return false;

        Process sam2Bam = Samtools.sam2BamParallel(path + name + ".sam", path + name + ".bam");
        if (!waitforProcess(sam2Bam, "sam2bam")) return false;
        new File(path + name + ".sam").delete();

        Process merge = Picard.mergeBamAlignment(bamUnmapped, path + name + ".bam", path + name + "_merged.bam", reference);
        if (!waitforProcess(merge, "Merge Bam Alignment")) return false;
        new File(path + name + ".bam").delete();
        new File(path + name + "_merged.bam").renameTo(new File(path + name + ".bam"));
        new File(path + name + "_merged.bai").renameTo(new File(path + name + ".bai"));

        return afterBwa(reference,path,name);
    }

    public static boolean getPreprocessedFrom2Interleaved(String bamUnmappedA, String bamUnmappedB, String fastqA, String fastqB,
                                                          String reference, String name, String nameA, String nameB, String path) throws IOException, InterruptedException {
        timer.start();

        System.out.println("(" + new Date().toString() + ") Start getting Bam from interleaved");
        Parameter[] parameters = {new Parameter('p', "")};

        BWAMEMAligner bwaA = new BWAMEMAligner(fastqA, "", reference, path + nameA + ".sam", path + nameA + ".log", parameters);
        BWAMEMAligner bwaB = new BWAMEMAligner(fastqB, "", reference, path + nameB + ".sam", path + nameB + ".log", parameters);
        if (!waitforProcess(bwaA.run(), "alineamiento bwa A")) return false;
        if (!waitforProcess(bwaB.run(), "alineamiento bwa B")) return false;

        Process sam2BamA = Samtools.sam2BamParallel(path + nameA + ".sam", path + nameA + ".bam");
        if (!waitforProcess(sam2BamA, "sam2bam A")) return false;
        Process sam2BamB = Samtools.sam2BamParallel(path + nameB + ".sam", path + nameB + ".bam");
        if (!waitforProcess(sam2BamB, "sam2bam B")) return false;
        new File(path + nameA + ".sam").delete();
        new File(path + nameB + ".sam").delete();

        Process mergeA = Picard.mergeBamAlignment(bamUnmappedA, path + nameA + ".bam", path + nameA + "_merged.bam", reference);
        Process mergeB = Picard.mergeBamAlignment(bamUnmappedB, path + nameB + ".bam", path + nameA + "_merged.bam", reference);
        if (!waitforProcess(mergeA, "Merge Bam Alignment A")) return false;
        if (!waitforProcess(mergeB, "Merge Bam Alignment B")) return false;
        new File(path + nameA + ".bam").delete();
        new File(path + nameA + "_merged.bam").renameTo(new File(path + nameA + ".bam"));
        new File(path + nameA + "_merged.bai").renameTo(new File(path + nameA + ".bai"));
        new File(path + nameB + ".bam").delete();
        new File(path + nameB + "_merged.bam").renameTo(new File(path + nameB + ".bam"));
        new File(path + nameB + "_merged.bai").renameTo(new File(path + nameB + ".bai"));

        Process sortA = Samtools.sortBamParallel(path + nameA + ".bam", path + nameA + ".sorted.bam");
        if (!waitforProcess(sortA, "sort A")) return false;
        Process indexA = Samtools.index( path + nameA + ".sorted.bam");
        if (!waitforProcess(indexA, "index sort A")) return false;
        Process sortB = Samtools.sortBamParallel(path + nameB + ".bam", path + nameB + ".sorted.bam");
        if (!waitforProcess(sortB, "sort B")) return false;
        Process indexB = Samtools.index( path + nameB + ".sorted.bam");
        if (!waitforProcess(indexB, "index sort B")) return false;

        new File(path + nameA + ".bam").delete();
        new File(path + nameA + ".bai").delete();
        new File(path + nameB + ".bam").delete();
        new File(path + nameB + ".bai").delete();

        Process merge = Samtools.merge(path + name + ".sorted.bam", path + nameA + ".sorted.bam", path + nameB + ".sorted.bam");
        if (!waitforProcess(merge, "Merging aligned")) return false;


        return afterBwa(reference,path,name);
    }

    public static boolean getPreprocessedFromPaired(String forward, String reverse, String reference, String name, String path) throws IOException, InterruptedException {
        timer.start();

        System.out.println("(" + new Date().toString() + ") Start preprocessing");
        Parameter[] parameters = {new Parameter('a', "")};

        BWAMEMAligner bwa = new BWAMEMAligner(forward, reverse, reference, path + name + ".sam", path + name + ".log", parameters);
        if (!waitforProcess(bwa.run(), "alineamiento bwa")) return false;
        Process sam2Bam = Samtools.sam2BamParallel(path + name + ".sam", path + name + ".bam");

        if (!waitforProcess(sam2Bam, "sam2bam")) return false;
        new File(path + name + ".sam").delete();

        return afterBwa(reference,path,name);
    }

    private static boolean afterBwa(String reference, String path, String name) throws IOException {
        Process sort = Samtools.sortBamParallel(path + name + ".bam", path + name + ".sorted.bam");
        if (!waitforProcess(sort, "sort")) return false;
        Process index = Samtools.index( path + name + ".sorted.bam");
        if (!waitforProcess(index, "index sort")) return false;
        new File(path + name + ".bam").delete();
        new File(path + name + ".bai").delete();

        Process mark = Picard.markDuplicates(path + name + ".sorted.bam", path + name + ".sortedDeDup.bam", path + name + ".dups");
        if (!waitforProcess(mark, "mark")) return false;
        new File(path + name + ".sorted.bam").delete();

        Process recall = GATK.BaseReacalibrator(reference, path + name + ".sortedDeDup.bam", path + name + "recall_data.table");
        if (!waitforProcess(recall, "recall")) return false;

        Process applyBQSR = GATK.PrintReads(reference, path + name + ".sortedDeDup.bam", path + name + "Final.bam", path +name+ "recall_data.table", path);
        if (!waitforProcess(applyBQSR, "Apply BQSR")) return false;
        new File(path + name + ".sortedDeDup.bam").delete();

        System.out.println("done");
        timer.reset();
        return true;

    }

    private static boolean waitforProcess(Process process, String name) {
        try {
            int status = process.waitFor();
            timer.stop();
            if (status == 0) System.out.println(name + " terminado con exito: " + timer.report());
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
