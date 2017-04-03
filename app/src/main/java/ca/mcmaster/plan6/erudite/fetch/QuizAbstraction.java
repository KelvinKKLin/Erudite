package ca.mcmaster.plan6.erudite.fetch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kelvin on 2017-04-03.
 */


public class QuizAbstraction {

    private String rawData;

    private String name = "";
    private String student = "";
    private String studentID = "";
    private ArrayList<String> questions = new ArrayList<String>();
    private ArrayList<String> answers = new ArrayList<String>();

    public QuizAbstraction() {
        rawData = "";
    }

    public QuizAbstraction(String rawData) {
        this.rawData = rawData;
        extractData();
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
        extractData();
    }

    private void extractData() {
        try {
            JSONObject jsonobject = new JSONObject(this.rawData);
            this.name = jsonobject.getString("name");
            this.student = jsonobject.getString("student");
            this.studentID = jsonobject.getString("student_id");
            this.questions.add(jsonobject.getString("q1"));
            this.questions.add(jsonobject.getString("q2"));
            this.questions.add(jsonobject.getString("q3"));
            this.answers.add(jsonobject.getString("a1"));
            this.answers.add(jsonobject.getString("a2"));
            this.answers.add(jsonobject.getString("a3"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logAllVariables();
    }

    public String getName(){
        return this.name;
    }

    public String getStudent(){
        return this.student;
    }

    public String getStudentID(){
        return this.studentID;
    }

    public ArrayList<String> getQuestions(){
        return this.questions;
    }

    public ArrayList<String> getAnswers(){
        return this.answers;
    }

    private void logAllVariables(){
        Log.v("DEBUG INFO", "Logging All Variables Now");
        Log.v("Name", this.name);
        Log.v("Student", this.student);
        Log.v("Student ID", this.studentID);

        for(String s : this.questions){
            Log.v("Question", s);
        }

        for(String s : this.answers){
            Log.v("Answer", s);
        }
    }

}
