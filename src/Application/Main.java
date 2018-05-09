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
    private final static String pathBam = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/BAM/";

    public static void main(String[] args) {
//        inverseEng();
        String forwardniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19/niv_19_pe_1.fq.gz";
        String reverseniv19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19/niv_19_pe_2.fq.gz";
        String pathFQ19 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19/";

        cleanBam(forwardniv19, reverseniv19, "niv19", pathFQ19);

        String forwardniv19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19_100x/niv19_forward.fastq.gz";
        String reverseniv19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19_100x/niv19_forward.fastq.gz";
        String pathFQ19100x = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV19_100x/";

        cleanBam(forwardniv19100x, reverseniv19100x, "niv19_100x", pathFQ19100x);

        String forwardniv32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV32/niv_32_pe_1.fq.gz";
        String reverseniv32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV32/niv_32_pe_2.fq.gz";
        String pathFQ32 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV32/";

        cleanBam(forwardniv32, reverseniv32, "niv32", pathFQ32);

        String forwardniv42 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV42/niv042-forward.fastq.gz";
        String reverseniv42 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV42/niv042-reverse.fastq.gz";
        String pathFQ42 = "/media/uichuimi/DiscoInterno/GENOME_DATA/NIV/FASTQ/NIV42/";

        cleanBam(forwardniv42, reverseniv42, "niv42", pathFQ42);
    }

    private static void cleanBam(String forward, String reverse, String name, String pathfastq) {
        String interleaved = pathfastq + name + "_interleaved.fq.gz";
        String ubam = pathfastq + name + ".ubam";

        try {
            if (!new File(interleaved).exists())
                PreProcessor.getInterleavedFastq(forward, reverse, name, pathfastq, name);
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
