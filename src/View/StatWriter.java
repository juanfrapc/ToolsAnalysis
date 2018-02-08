package View;

import Model.AlignmentsStatistics;
import Model.Parameter;

public interface StatWriter {

    void write(Parameter[] parameters, AlignmentsStatistics statistics);

}
