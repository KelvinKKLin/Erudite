package ca.mcmaster.plan6.erudite.grades;

import java.util.ArrayList;

/**
 * Created by Kelvin on 2017-04-06.
 */

public abstract class GradesAbstraction{

    /**
     * This method sets the raw data of the abstraction
     * @param rawData   Unprocessed data
     */
    public abstract void setRawData(String rawData);

    /**
     * This method returns the success status of the server.
     * @return  Success Status
     */
    public abstract String getSuccess();

    /**
     * This method returns any accompanying messages from the server.
     * @return  Accompanying messages
     */
    public abstract String getMessage();

    /**
     * This method returns a list of grades accosiated with the account
     * @return  List of grades
     */
    public abstract ArrayList<String> getGrades();

    /**
     * This method returns a list of names associated with the account.
     * It could be a the names of assignments or students, depending on the implementation.
     * @return  List of names
     */
    public abstract ArrayList<String> getNames();

    /**
     * This method returns a list of grades associated with the account
     * @return  List of grades
     */
    public abstract ArrayList<Double> getGradeValues();

}
