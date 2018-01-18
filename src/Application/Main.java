package Application;

import Model.Alignment;
import Parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        String path = "/media/uichuimi/DiscoInterno/GENOME_DATA/CONTROLS/DAM/C7BDUACXX_8_3ss.bam";
        Stream<Alignment> stream;
//        stream = new Parser().parseBam(new File(path));
//        long nAlignments = stream.parallel().count();
//        System.out.println(nAlignments);

        stream = new Parser().parseBam(new File(path));
        long count = stream.parallel().map(alignment -> Integer.parseInt(alignment.getParam(Alignment.FLAG)))
                .map(Integer::toBinaryString)
                .filter(flag -> flag.charAt(flag.length() - 3) == '1')
                .count();
        System.out.println(count);
    }

}
