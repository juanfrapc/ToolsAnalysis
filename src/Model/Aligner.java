package Model;

import java.io.File;
import java.io.IOException;

public interface Aligner {

    default Process run(){
        ProcessBuilder pb = buildCmd();
        pb = pb.redirectOutput(ProcessBuilder.Redirect.to(getOutput()));
        pb = pb.redirectError(ProcessBuilder.Redirect.to(getLog()));
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    default String getID(){
        return "aligner";
    }

    Parameter[] getParameterS();
    ProcessBuilder buildCmd();
    File getOutput();
    File getLog();

}
