package Control;

import Application.Aligners.BWABackTrackAlnAligner;
import Application.Aligners.BWABackTrackSampe;
import Application.Aligners.BWAMEMAligner;
import Application.Aligners.BWASWAligner;
import Model.Aligner;
import Model.Parameter;
import Model.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PreProcessor {

    private static Timer timer = new Timer();

    public static boolean getInterleavedFastq(String forward, String reverse, String name, String path, String seqName) throws IOException {
        timer.start();
        System.out.println("(" + new Date().toString() + ") Start getting interleaved");

        Process fastq2sam = Picard.fastQ2Sam(forward, reverse, path + name + ".ubam", seqName, name);
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
        new File(path + name + ".log").delete();

        Process merge = Picard.mergeBamAlignment(bamUnmapped, path + name + ".bam", path + name + "_merged.bam", reference);
        if (!waitforProcess(merge, "Merge Bam Alignment")) return false;
        new File(path + name + ".bam").delete();
        new File(path + name + "_merged.bam").renameTo(new File(path + name + ".bam"));
        new File(path + name + "_merged.bai").renameTo(new File(path + name + ".bam.bai"));

        return afterBwa(reference, path, name);
    }

    public static boolean getPreprocessedFrom2Interleaved(String bamUnmappedA, String bamUnmappedB, String fastqA, String fastqB,
                                                          String reference, String name, String nameA, String nameB, String path) throws IOException, InterruptedException {
        timer.start();

        System.out.println("(" + new Date().toString() + ") Start getting Bam from interleaveds");
        Parameter[] parameters = {new Parameter('p', "")};

        BWAMEMAligner bwaA = new BWAMEMAligner(fastqA, "", reference, path + nameA + ".sam", path + nameA + ".log", parameters);
        if (!waitforProcess(bwaA.run(), "alineamiento bwa A")) return false;
        BWAMEMAligner bwaB = new BWAMEMAligner(fastqB, "", reference, path + nameB + ".sam", path + nameB + ".log", parameters);
        if (!waitforProcess(bwaB.run(), "alineamiento bwa B")) return false;

        Process sam2BamA = Samtools.sam2BamParallel(path + nameA + ".sam", path + nameA + ".bam");
        if (!waitforProcess(sam2BamA, "sam2bam A")) return false;
        Process sam2BamB = Samtools.sam2BamParallel(path + nameB + ".sam", path + nameB + ".bam");
        if (!waitforProcess(sam2BamB, "sam2bam B")) return false;
        new File(path + nameA + ".sam").delete();
        new File(path + nameA + ".log").delete();
        new File(path + nameB + ".sam").delete();
        new File(path + nameB + ".log").delete();

        Process mergeA = Picard.mergeBamAlignment(bamUnmappedA, path + nameA + ".bam", path + nameA + ".merged.bam", reference);
        Process mergeB = Picard.mergeBamAlignment(bamUnmappedB, path + nameB + ".bam", path + nameB + ".merged.bam", reference);
        if (!waitforProcess(mergeA, "Merge Bam Alignment A")) return false;
        if (!waitforProcess(mergeB, "Merge Bam Alignment B")) return false;
        new File(path + nameA + ".bam").delete();
        new File(path + nameA + ".merged.bam").renameTo(new File(path + nameA + ".bam"));
        new File(path + nameA + ".merged.bai").renameTo(new File(path + nameA + ".bai"));
        new File(path + nameB + ".bam").delete();
        new File(path + nameB + ".merged.bam").renameTo(new File(path + nameB + ".bam"));
        new File(path + nameB + ".merged.bai").renameTo(new File(path + nameB + ".bai"));

        Process sortA = Samtools.sortBamParallel(path + nameA + ".bam", path + nameA + ".sorted.bam");
        if (!waitforProcess(sortA, "sort A")) return false;
        Process indexA = Samtools.index(path + nameA + ".sorted.bam");
        if (!waitforProcess(indexA, "index sort A")) return false;
        Process sortB = Samtools.sortBamParallel(path + nameB + ".bam", path + nameB + ".sorted.bam");
        if (!waitforProcess(sortB, "sort B")) return false;
        Process indexB = Samtools.index(path + nameB + ".sorted.bam");
        if (!waitforProcess(indexB, "index sort B")) return false;

        new File(path + nameA + ".bam").delete();
        new File(path + nameA + ".bai").delete();
        new File(path + nameB + ".bam").delete();
        new File(path + nameB + ".bai").delete();

        Process merge = Samtools.merge(path + name + ".bam", path + nameA + ".sorted.bam", path + nameB + ".sorted.bam");
        if (!waitforProcess(merge, "Merging aligned")) return false;
        Process indexMerged = Samtools.index(path + name + ".bam");
        if (!waitforProcess(indexMerged, "index sort merged")) return false;

        new File(path + nameA + ".sorted.bam").delete();
        new File(path + nameA + ".sorted.bam.bai").delete();
        new File(path + nameB + ".sorted.bam").delete();
        new File(path + nameB + ".sorted.bam.bai").delete();

        return afterBwa(reference, path, name);
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

        return afterBwa(reference, path, name);
    }

    public static boolean getPreprocessedFromInterleavedParm(
            String bamUnmapped, String fastq, String reference, String name, String path, Parameter[] parameters, String alignerType)
            throws IOException, InterruptedException {
        timer.start();

        System.out.println("(" + new Date().toString() + ") Start getting Bam from interleaved");

        Aligner bwa = null;
        switch (alignerType) {
            case "MEM":
                Parameter[] execParam = new Parameter[parameters.length + 1];
                System.arraycopy(parameters, 0, execParam, 0, parameters.length);
                execParam[parameters.length] = new Parameter('p', "");
                bwa = new BWAMEMAligner(fastq, "", reference, path + name + ".sam", path + name + ".log", execParam);
                if (!waitforProcess(bwa.run(), "alineamiento bwa")) return false;
                break;
            case "SW":
                String fastqPath = path.split("BAM")[0] + "FASTQ/";
                bwa = new BWASWAligner(fastqPath + name.replace("_GEN", "") + "_forward.fastq.gz", fastqPath + name.replace("_GEN", "") + "_reverse.fastq.gz",
                        reference, path + name + ".sam", path + name + ".log", parameters);
                if (!waitforProcess(bwa.run(), "alineamiento bwa")) return false;

                Process clean = Picard.clean(path + name + ".sam", path + name + "_clean.sam");
                if (!waitforProcess(clean, "cleaning")) return false;
                new File(path + name + ".sam").delete();
                new File(path + name + "_clean.sam").renameTo(new File(path + name + ".sam"));
                break;
            case "ALN":
                fastqPath = path.split("BAM")[0] + "FASTQ/";
                bwa = new BWABackTrackAlnAligner(fastqPath + name.replace("_GEN", "") + "_forward.fastq.gz",
                        reference, path + name + "forward.sai", path + name + ".log", parameters);
                if (!waitforProcess(bwa.run(), "alineamiento bwa sai forward")) return false;
                bwa = new BWABackTrackAlnAligner(fastqPath + name.replace("_GEN", "") + "_reverse.fastq.gz",
                        reference, path + name + "reverse.sai", path + name + ".log", parameters);
                if (!waitforProcess(bwa.run(), "alineamiento bwa sai reverse")) return false;
                bwa = new BWABackTrackSampe(path + name + "forward.sai",
                        fastqPath + name.replace("_GEN", "") + "_forward.fastq.gz",
                        path + name + "reverse.sai",
                        fastqPath + name.replace("_GEN", "") + "_reverse.fastq.gz",
                        reference, path + name + ".sam", path + name + ".log");
                if (!waitforProcess(bwa.run(), "alineamiento bwa sampe")) return false;

                clean = Picard.clean(path + name + ".sam", path + name + "_clean.sam");
                if (!waitforProcess(clean, "cleaning")) return false;
                new File(path + name + ".sam").delete();
                new File(path + name + "_clean.sam").renameTo(new File(path + name + ".sam"));
                break;
        }

        Process sam2Bam = Samtools.sam2BamParallel(path + name + ".sam", path + name + ".bam");
        if (!waitforProcess(sam2Bam, "sam2bam")) return false;
        new File(path + name + ".sam").delete();
        new File(path + name + ".log").delete();

        Process merge = Picard.mergeBamAlignment(bamUnmapped, path + name + ".bam", path + name + "_merged.bam", reference);
        if (!waitforProcess(merge, "Merge Bam Alignment")) return false;
        new File(path + name + ".bam").delete();
        new File(path + name + "_merged.bam").renameTo(new File(path + name + ".bam"));
        new File(path + name + "_merged.bai").renameTo(new File(path + name + ".bam.bai"));
        return afterBwa(reference, path, name);
    }

    private static boolean afterBwa(String reference, String path, String name) throws IOException {
        Process sort = Samtools.sortBamParallel(path + name + ".bam", path + name + ".sorted.bam");
        if (!waitforProcess(sort, "sort")) return false;
        Process index = Samtools.index(path + name + ".sorted.bam");
        if (!waitforProcess(index, "index sort")) return false;
        new File(path + name + ".bam").delete();
        new File(path + name + ".bam.bai").delete();

        Process mark = Picard.markDuplicates(path + name + ".sorted.bam", path + name + ".sortedDeDup.bam", path + name + ".dups");
        if (!waitforProcess(mark, "mark")) return false;
        new File(path + name + ".sorted.bam").delete();
        new File(path + name + ".sorted.bam.bai").delete();

        Process recall = GATK.baseReacalibrator(reference, path + name + ".sortedDeDup.bam", path + name + ".recall_data.table");
        if (!waitforProcess(recall, "recall")) return false;

        Process applyBQSR = GATK.applyBQSR(reference, path + name + ".sortedDeDup.bam", path + name + ".bam", path + name + ".recall_data.table", path);
        if (!waitforProcess(applyBQSR, "Apply BQSR")) return false;
        new File(path + name + ".sortedDeDup.bam").delete();

        System.out.println("********************\ndone with getting BAM\n******************\n");
        timer.reset();
        return true;

    }

    private static boolean waitforProcess(Process process, String name) {
        try {
            int status = process.waitFor();
            timer.stop();
            if (status == 0)
                System.out.println("*********************\n " + name + " terminado con exito: " + timer.report());
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
