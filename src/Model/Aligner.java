package Model;

import java.io.File;
import java.io.IOException;

public interface Aligner {

    default Process run(){
        ProcessBuilder pb = buildCmd();
        pb = pb.redirectOutput(ProcessBuilder.Redirect.to(getOutputPath()));
        pb = pb.redirectError(ProcessBuilder.Redirect.to(getLogPath()));
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
