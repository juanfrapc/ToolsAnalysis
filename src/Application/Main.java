package Application;

import Application.AlignningStatsTasks.BWABackTrackTask;
import Application.AlignningStatsTasks.BWAMEMTask;
import Application.AlignningStatsTasks.BWASWTask;
import Control.GermlineSNP;
import Control.PreProcessor;
import Model.Parameter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Main {

    private final static String reference = "/media/uichuimi/DiscoInterno/GENOME_DATA/REFERENCE/gatk_resourcebunde_GRCH38.fasta";

    public static void main(String[] args) throws IOException {
//        inverseEng();
//        String forwardniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19/FASTQ/niv_19_pe_1.fq.gz";
//        String reverseniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19/FASTQ/niv_19_pe_2.fq.gz";
//        String path19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19/";
//        cleanBam(forwardniv19, reverseniv19,"niv19", "niv19", path19);

        String name = null;
        String forward = null;
        String reverse = null;
        String forward_1 = null;
        String reverse_1 = null;
        String path = null;
        if (args.length != 4 && args.length != 6) {
            System.out.println("error en parámetros");
            return;
        }
        for (String arg : args) {
            if (arg.contains("NAME")) name = arg.split("=")[1] + "_" +
                    DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()) + "_" +
                    "GATK_GRCH38";
            if (arg.contains("FQ1")) forward = arg.split("=")[1];
            if (arg.contains("FQ2")) reverse = arg.split("=")[1];
            if (arg.contains("FQ3")) forward_1 = arg.split("=")[1];
            if (arg.contains("FQ4")) reverse_1 = arg.split("=")[1];
            if (arg.contains("PATH")) path = arg.split("=")[1];
        }
        if (args.length == 6) {
            String ubamNameA = forward.substring(forward.lastIndexOf("/") + 1, forward.lastIndexOf("_"));
            String ubamNameB = forward_1.substring(forward_1.lastIndexOf("/") + 1, forward.lastIndexOf("_"));
            if (!path.contains("CONTROLS")) cleanBam(forward, reverse, forward_1, reverse_1, ubamNameA, ubamNameB, name, path);
            GermlineSNP.getVCFilteredFromMultiBAM(reference, name, path + "BAM/", path + "VCF/", ubamNameA, ubamNameB);
        } else {
            String ubamName = forward.substring(forward.lastIndexOf("/") + 1, forward.lastIndexOf("_"));
            if (!path.contains("CONTROLS")) cleanBam(forward, reverse, ubamName, name, path);
            GermlineSNP.getVCFilteredFromSingelBAM(reference, name, path + "BAM/", path + "VCF/");
        }
    }

    private static void cleanBam(String forward, String reverse, String ubamName, String name, String path) {
        String interleaved = path + "FASTQ/" + ubamName + "_interleaved.fq.gz";
        String ubam = path + "FASTQ/" + ubamName + ".ubam";
        String pathFQ = path + "FASTQ/";
        String pathBam = path + "BAM/";

        try {
            if (!new File(interleaved).exists())
                PreProcessor.getInterleavedFastq(forward, reverse, ubamName, pathFQ, name);
            PreProcessor.getPreprocessedFromInterleaved(ubam, interleaved, reference, name, pathBam);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void cleanBam(String forwardA, String reverseA, String forwardB, String reverseB,
                                 String ubamNameA, String ubamNameB, String name, String path) {
        String interleavedA = path + "FASTQ/" + ubamNameA + "_interleaved.fq.gz";
        String ubamA = path + "FASTQ/" + ubamNameA + ".ubam";
        String interleavedB = path + "FASTQ/" + ubamNameB + "_interleaved.fq.gz";
        String ubamB = path + "FASTQ/" + ubamNameB + ".ubam";

        String pathFQ = path + "FASTQ/";
        String pathBam = path + "BAM/";
        try {
            if (!new File(interleavedA).exists())
                PreProcessor.getInterleavedFastq(forwardA, reverseA, ubamNameA, pathFQ, name);
            if (!new File(interleavedB).exists())
                PreProcessor.getInterleavedFastq(forwardB, reverseB, ubamNameB, pathFQ, name);
            PreProcessor.getPreprocessedFrom2Interleaved(ubamA, ubamB, interleavedA, interleavedB,
                    reference, name, ubamNameA, ubamNameB, pathBam);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void inverseEng() {
        String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/DAM/FASTQ/DAM_forward.fastq.gz";
        String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/DAM/FASTQ/DAM_reverse.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/GENOME_DATA/REFERENCE/genome.fasta";
        runBWAMEMCases(forwardPath, reversePath, reference);
        System.out.println("-------------------------------------------------------------------");
        runBWABacktrackCases(forwardPath, reversePath, reference);
        System.out.println("-------------------------------------------------------------------");
        runBWASWCases(forwardPath, reversePath, reference);
        System.out.println("-------------------------------------------------------------------");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static void runBWASWCases(String forwardPath, String reversePath, String reference) {
        String name = "defaultSW";
        Parameter[] parameters = new Parameter[0];
        BWASWTask task = new BWASWTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "sw1";
        parameters = new Parameter[]{
                new Parameter('a', "5"),
                new Parameter('q', "3"),
                new Parameter('r', "1"),
                new Parameter('z', "2"),
                new Parameter('s', "4"),
        };
        task = new BWASWTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "sw2";
        parameters = new Parameter[]{
                new Parameter('a', "3"),
                new Parameter('q', "8"),
                new Parameter('r', "4"),
                new Parameter('z', "2"),
                new Parameter('s', "4"),
        };
        task = new BWASWTask(name, forwardPath, reversePath, reference, parameters);
        task.run();
    }

    private static void runBWABacktrackCases(String forwardPath, String reversePath, String reference) {
        String name = "defaultALN";
        Parameter[] parameters = new Parameter[0];

        BWABackTrackTask task = new BWABackTrackTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "aln1";
        parameters = new Parameter[]{
                new Parameter('o', "3"),
                new Parameter('k', "4"),
                new Parameter('O', "6"),
                new Parameter('E', "3"),
        };
        task = new BWABackTrackTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "aln2";
        parameters = new Parameter[]{
                new Parameter('o', "2"),
                new Parameter('k', "3"),
                new Parameter('O', "15"),
                new Parameter('E', "6"),
        };
        task = new BWABackTrackTask(name, forwardPath, reversePath, reference, parameters);
        task.run();
    }

    private static void runBWAMEMCases(String forwardPath, String reversePath, String reference) {
        String name = "defaultMEM";
        Parameter[] parameters = new Parameter[0];
        BWAMEMTask task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "mem1";
        parameters = new Parameter[]{
                new Parameter('r', "1"),
                new Parameter('w', "100"),
                new Parameter('A', "10"),
                new Parameter('O', "4"),
                new Parameter('E', "1"),
        };
        task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

        name = "mem2";
        parameters = new Parameter[]{
                new Parameter('r', "1"),
                new Parameter('w', "50"),
                new Parameter('A', "6"),
                new Parameter('O', "4"),
                new Parameter('E', "2"),
        };
        task = new BWAMEMTask(name, forwardPath, reversePath, reference, parameters);
        task.run();

    }

}
