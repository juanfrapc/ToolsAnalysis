package Tests;

import Model.Alignment;
import org.junit.Test;
import parse.BamParser;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class SamParserTest {

    final File file = new File("../ejemplo.sam");
    final Stream<Alignment> stream = new BamParser().parseSam(file);

    public SamParserTest() throws IOException {
    }

    public boolean checkInteger(String str){
        try{
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    @Test
    public void testFIleShow() throws IOException {
        stream.parallel().forEach(alignment -> {
            System.out.println(alignment);
        });
    }

    @Test
    public void paramTest() {
        stream.parallel().forEach(alignment ->{
            assert(alignment.getSize() == 11);
            assert(checkInteger(alignment.getParam(Alignment.FLAG)));
            assert(checkInteger(alignment.getParam(Alignment.MAPQ)));
            assert(checkInteger(alignment.getParam(Alignment.PNEXT)));
            assert(checkInteger(alignment.getParam(Alignment.POS)));
            assert(checkInteger(alignment.getParam(Alignment.TLEN)));
        });
    }

}
