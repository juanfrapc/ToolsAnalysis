package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;
import View.Parser.Sam2StatsParser;

class Main {

    public static void main(String[] args) {

        String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_1.fastq.gz";
        String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_2.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";

//        runBWAMEMCases(forwardPath, reversePath, reference);
//        System.out.println("-------------------------------------------------------------------");
//        runBWABacktrackCases(forwardPath, reversePath, reference);
//        System.out.println("-------------------------------------------------------------------");
//        runBWASWCases(forwardPath, reversePath, reference);
//        System.out.println("-------------------------------------------------------------------");
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

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
