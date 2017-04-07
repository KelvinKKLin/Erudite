package ca.mcmaster.plan6.erudite;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Puru on 2017-04-03.
 * Modified by Kelvin on 2017-04-03.
 */

public class ClassGradesAbstraction extends GradesAbstraction{

    /**
     * The unprocessed Grade data
     */
    private String rawData;

    /**
     * The success status of the server
     */
    private String success      = "";

    /**
     * Accompanying server messages
     */
    private String message      = "";

    /**
     * A list of student names
     */
    private ArrayList<String> names       = new ArrayList<String>();

    /**
     * A list of student name-value pairs
     */
    private ArrayList<String> grades      = new ArrayList<String>();

    /**
     * A list of grade names
     */
    private ArrayList<String> gradeNames  = new ArrayList<String>();

    /**
     * A list of grade values
     */
    private ArrayList<Double> gradeValues = new ArrayList<Double>();

    /**
     * GradeAbstraction for instances without raw data
     */
    public ClassGradesAbstraction(){
        this.rawData = "";
    }

    /**
     * GradeAbstration for instances with raw data
     * @param rawData   Unprocessed grade data
     */
    public ClassGradesAbstraction(String rawData){
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method updates the raw data in the GradeAbstraction
     * @param rawData   Unprocessed grade data
     */
    public void setRawData(String rawData){
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method processes the raw data and sets the appropriate variables into the corresponding
     * variable fields.
     */
    private void extractData(){
        try{
            JSONObject jsonobject = new JSONObject(this.rawData);
            this.success = jsonobject.getString("success");
            this.message = jsonobject.getString("message");

            JSONArray studentsData = jsonobject.getJSONArray("students");
            names.add(studentsData.getString(0));

            JSONArray studentGrades = studentsData.getJSONArray(2);
            for(int i = 0; i < studentGrades.length(); i++){
                String[] tempArr = studentGrades.getString(i).split(":");
                this.grades.add(tempArr[0] + ": " + tempArr[1]);
            }
            extractGradeData(this.grades);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //logAllVariables();
    }


    /**
     * This method processes the grade data
     * @param grades    Unprocessed grade data
     */
    private void extractGradeData(ArrayList<String> grades){
        for(String str : grades){
            String[] strArr = str.split(":");
            this.gradeNames.add(strArr[0]);
            this.gradeValues.add(Double.parseDouble(strArr[1]));
        }
    }

    /**
     * This method outputs all state variables to the logcat.
     * Used for debugging.
     */
    private void logAllVariables(){
            Log.v("DEBUG INFO", "Logging All Variables Now");
            Log.v("Success", this.success);
            Log.v("Message", this.message);
            for(String s : this.grades){
                Log.v("GRADE", s);
            }
            for(String s : this.gradeNames){
                Log.v("GRADE NAME", s);
            }
            for(Double d : this.gradeValues){
                Log.v("GRADE VALUE", String.valueOf(d));
            }
    }

    /**
     * This method returns a list of student names
     * @return  List of student names
     */
    public ArrayList<String> getNames(){
        return this.names;
    }

    /**
     * This method returns the user's name-grade pair
     * @return  User's name-grade pair
     */
    public ArrayList<String> getGrades() {
        return this.grades;
    }

    /**
     * This method returns the user's list of assignment names
     * @return  List of assignment names
     */
    public ArrayList<String> getGradeNames(){
        return this.gradeNames;
    }

    /**
     * This method returns the user's list of grade values
     * @return  List of grade values
     */
    public ArrayList<Double> getGradeValues(){
        return this.gradeValues;
    }

    /**
     * This method returns the server's success status
     * @return  Server's success status
     */
    public String getSuccess(){
        return this.success;
    }

    /**
     * This method returns the server's accompanying message
     * @return  Server's accompanying message
     */
    public String getMessage(){
        return this.message;
    }


}
