package Application;

import Model.AlignmentsStatistics;
import Model.Parameter;

public interface AligningTask {

    AlignmentsStatistics run();

    void setParameters(Parameter[] parameters);
}