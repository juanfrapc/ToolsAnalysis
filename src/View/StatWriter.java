package View;

import Model.Parameter;
import Model.Statistics;

public interface StatWriter {

    void write(Parameter[] parameters, Statistics statistics);

}
