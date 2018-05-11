package Application;

import Application.AlignningStatsTasks.BWABackTrackTask;
import Application.AlignningStatsTasks.BWAMEMTask;
import Application.AlignningStatsTasks.BWASWTask;
import Control.PreProcessor;
import Model.Parameter;

import java.io.File;
import java.io.IOException;

class Main {

    private final static String reference = "/media/uichuimi/DiscoInterno/GENOME_DATA/REFERENCE/gatk_resourcebunde_GRCH38.fasta";

    public static void main(String[] args) {
//        inverseEng();
        String forwardniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19/FASTQ/niv_19_pe_1.fq.gz";
        String reverseniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19/FASTQ/niv_19_pe_2.fq.gz";
        String path19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19/";

        cleanBam(forwardniv19, reverseniv19, "niv19v37", path19);

        String forwardniv19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19-100X/FASTQ/niv19_forward.fastq.gz";
        String reverseniv19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19-100X/FASTQ/niv19_forward.fastq.gz";
        String path19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV19-100x/";

        cleanBam(forwardniv19100x, reverseniv19100x, "niv19_100Xv37", path19100x );

        String forwardniv32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV32/FASTQ/niv_32_pe_1.fq.gz";
        String reverseniv32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV32/FASTQ/niv_32_pe_2.fq.gz";
        String path32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV32/";

        cleanBam(forwardniv32, reverseniv32, "niv32v37", path32);

        String forwardniv42 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV42/FASTQ/niv042-forward-sorted-clean.fastq.gz";
        String reverseniv42 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV42/FASTQ/niv042-reverse-sorted-clean.fastq.gz";
        String path42 = "//media/uichuimi/DiscoInterno/GENOME_DATA/NIV/NIV42/";

        cleanBam(forwardniv42, reverseniv42, "niv42v37", path42);
    }

    private static void cleanBam(String forward, String reverse, String name, String path) {
        String interleaved = path + "FASTQ/" + name + "_interleaved.fq.gz";
        String ubam = path + "FASTQ/" + name + ".ubam";
        String pathFQ = path + "FASTQ/";
        String pathBam = path + "BAM/";

        try {
            if (!new File(interleaved).exists())
                PreProcessor.getInterleavedFastq(forward, reverse, name, pathFQ, name);
            PreProcessor.getPreprocessedFromInterleaved(ubam, interleaved, reference, name, pathBam);
//            boolean status = PreProcessor.getPreprocessedFromPaired(forwardPath,
//                    reversePath, reference,
//                    "NIV19", "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/BAM/");
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
