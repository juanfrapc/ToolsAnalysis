package Model;

import java.io.IOException;

public interface Aligner {

    Process run() throws IOException;
    String getID();
    Parameter[] getParameter();



}
