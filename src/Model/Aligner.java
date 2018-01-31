package Model;

import java.io.File;
import java.io.IOException;

public interface Aligner {

    default Process run(){
        ProcessBuilder pb = buildCmd();
        pb = pb.redirectOutput(getOutputPath());
        pb = pb.redirectError(getLogPath());
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
    File getOutputPath();
    File getLogPath();

}
