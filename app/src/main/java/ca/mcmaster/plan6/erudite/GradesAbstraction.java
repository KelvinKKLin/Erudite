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

public class GradesAbstraction {

    private String rawData;

    private String success      = "";
    private String message      = "";
    private String userID       = "";
    private String email        = "";
    private String password     = "";
    private String account_type = "";
    private String courses      = "";

    private ArrayList<String> grades      = new ArrayList<String>();
    private ArrayList<String> gradeNames  = new ArrayList<String>();
    private ArrayList<Double> gradeValues = new ArrayList<Double>();

    public GradesAbstraction(String rawData){
        this.rawData = rawData;
        extractData();
    }

    public GradesAbstraction(){
        this.rawData = "";
    }

    private void extractData(){
        try{
            Log.v("Raw Data", this.rawData);
            JSONObject jsonobject = new JSONObject(this.rawData);
            this.success = jsonobject.getString("success");
            this.message = jsonobject.getString("message");

            JSONObject userdata = new JSONObject(jsonobject.getString("user"));
            extractUserData(userdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        logAllVariables();
    }

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

    private void extractGradeData(ArrayList<String> grades){
        for(String str : grades){
            String[] strArr = str.split(":");
            this.gradeNames.add(strArr[0]);
            this.gradeValues.add(Double.parseDouble(strArr[1]));
        }
    }

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

    public ArrayList<String> getGrades() {
        return this.grades;
    }

    public ArrayList<String> getGradeNames(){
        return this.gradeNames;
    }

    public ArrayList<Double> getGradeValues(){
        return this.gradeValues;
    }

    public String getSuccess(){
        return this.success;
    }

    public String getMessage(){
        return this.message;
    }

    public String getUserID(){
        return this.userID;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getAccountType(){
        return this.account_type;
    }

    public String getCourses(){
        return this.courses;
    }

}
