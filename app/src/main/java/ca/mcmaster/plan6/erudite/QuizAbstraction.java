package ca.mcmaster.plan6.erudite;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kelvin on 2017-04-03.
 */


public class QuizAbstraction {

    /**
     * The unprocessed Grades data
     */
    private String rawData;

    /**
     * The name of the quiz.
     */
    private String name = "";

    /**
     * The email of the account taking the quiz
     */
    private String student = "";

    /**
     * The ID of the user taking the quiz
     */
    private String studentID = "";

    /**
     * The quiz questions
     */
    private ArrayList<String> questions = new ArrayList<String>();

    /**
     * The quiz answers
     */
    private ArrayList<String> answers = new ArrayList<String>();

    /**
     * QuizAbstraction constructor for instances with no raw data
     */
    public QuizAbstraction() {
        rawData = "";
    }

    /**
     * QuizAbstraction constructor for instances with raw data
     * @param rawData   Unprocessed quiz data
     */
    public QuizAbstraction(String rawData) {
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method sets the rawData variable with raw data
     * @param rawData   Unprocessed quiz data
     */
    public void setRawData(String rawData) {
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method processes the raw data and assigns values to the corresponding variables
     * within the class.
     */
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
        //logAllVariables();
    }

    /**
     * This method returns the name of the quiz
     * @return  The name of the quiz
     */
    public String getName(){
        return this.name;
    }

    /**
     * This method returns the user's email
     * @return  The user's email
     */
    public String getStudent(){
        return this.student;
    }

    /**
     * This method returns the user's ID
     * @return  The user's ID
     */
    public String getStudentID(){
        return this.studentID;
    }

    /**
     * This method returns the quiz questions
     * @return  The quiz questions
     */
    public ArrayList<String> getQuestions(){
        return this.questions;
    }

    /**
     * This method returns the quiz answers
     * @return  The quiz answers
     */
    public ArrayList<String> getAnswers(){
        return this.answers;
    }

    /**
     * This method outputs the value of all state variables to the logcat.
     * Used for debugging.
     */
    private void logAllVariables(){
        Log.v("DEBUG INFO", "Logging All Variables Now");
        Log.v("Name", this.name);
        Log.v("Student", this.student);
        Log.v("Student ID", this.studentID);

        //Output all questions
        for(String s : this.questions){
            Log.v("Question", s);
        }

        //Output all answers
        for(String s : this.answers){
            Log.v("Answer", s);
        }
    }

}
