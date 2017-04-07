package ca.mcmaster.plan6.erudite;

import java.util.ArrayList;

/**
 * Created by Kelvin on 2017-04-06.
 */

public abstract class GradesAbstraction{

    public abstract void setRawData(String rawData);
    public abstract String getSuccess();
    public abstract String getMessage();
    public abstract ArrayList<String> getGrades();
    public abstract ArrayList<String> getGradeNames();
    public abstract ArrayList<Double> getGradeValues();

}
