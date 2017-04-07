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

public class SimpleGradesAbstraction extends GradesAbstraction{

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
     * The user's ID
     */
    private String userID       = "";

    /**
     * The user's email
     */
    private String email        = "";

    /**
     * The user's encrypted password
     */
    private String password     = "";

    /**
     * The user's account type (teacher or student)
     */
    private String account_type = "";

    /**
     * The user's courses
     */
    private String courses      = "";

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
    public SimpleGradesAbstraction(){
        this.rawData = "";
    }

    /**
     * GradeAbstration for instances with raw data
     * @param rawData   Unprocessed grade data
     */
    public SimpleGradesAbstraction(String rawData){
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

            JSONObject userdata = new JSONObject(jsonobject.getString("user"));
            extractUserData(userdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //logAllVariables();
    }

    /**
     * This method processes the user's data
     * @param userdata  Unprocessed user data
     */
    private void extractUserData(JSONObject userdata){
        try {
            this.userID = userdata.getString("_id");
            this.email = userdata.getString("email");
            this.password = userdata.getString("password");
            this.account_type = userdata.getString("account_type");
            this.courses = userdata.getString("courses");

            JSONArray userGrades = userdata.getJSONArray("grades");
            for(int i = 0; i < userGrades.length(); i++){
                String[] tempArr = userGrades.getString(i).split(":");
                this.grades.add(tempArr[0] + ": " + tempArr[1]);
            }

            extractGradeData(this.grades);
        } catch(JSONException e){
            e.printStackTrace();
        }
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
            Log.v("UserID", this.userID);
            Log.v("Email", this.email);
            Log.v("Password", this.password);
            Log.v("Account Type", this.account_type);
            Log.v("Courses", this.courses);
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
    public ArrayList<String> getNames(){
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

    /**
     * This method returns the user's ID
     * @return  User's ID
     */
    public String getUserID(){
        return this.userID;
    }

    /**
     * This method returns the user's email
     * @return  User's email
     */
    public String getEmail(){
        return this.email;
    }

    /**
     * This method returns the user's encrypted password
     * @return  User's encrypted password
     */
    public String getPassword(){
        return this.password;
    }

    /**
     * This method returns the user's account type
     * @return  User's account type
     */
    public String getAccountType(){
        return this.account_type;
    }

    /**
     * This method returns the user's courses
     * @return  User's courses
     */
    public String getCourses(){
        return this.courses;
    }

}
