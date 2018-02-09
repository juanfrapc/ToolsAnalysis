package Application;

import Model.Parameter;

class Main {

    public static void main(String[] args) {

        String forwardPath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_1.fastq.gz";
        String reversePath = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss_2.fastq.gz";
        String reference = "/media/uichuimi/DiscoInterno/references/GRCh38/GRCh38.fa";

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
                new Parameter('E', ""),
        };
        task = new BWABackTrackTask(name, forwardPath, reversePath, reference, parameters);
        task.run();



    }

}
