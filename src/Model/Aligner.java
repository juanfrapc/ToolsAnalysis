package Model;

import java.io.File;
import java.io.IOException;

public interface Aligner {

    Parameter[] getParameters();
    ProcessBuilder buildAlignerCmd();
    File getOutput();
    File getLog();

    default String getID(){
        return "aligner";
    }

    default Process runSimple(){
        ProcessBuilder pb = buildAlignerCmd();
        pb = pb.redirectOutput(ProcessBuilder.Redirect.to(getOutput()));
        pb = pb.redirectError(ProcessBuilder.Redirect.to(getLog()));
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
