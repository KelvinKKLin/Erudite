package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

/**
 * Created by Varun on 2014-04-01.
 * Modified by Kelvin on 2017-04-03.
 */

public class QuizzesActivity extends Activity {

    /**
     * This method initializes the Activity
     * @param savedInstanceState    The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizzes_activity);
    }

    /**
     * This method defines the behaviour of the Quiz subsystem after initialization
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Model Variables
        final QuizAbstraction qa = new QuizAbstraction();  //The quiz abstraction

        //View Variables
        //Question fields
        final TextView question1 = (TextView) findViewById(R.id.question1);
        final TextView question2 = (TextView) findViewById(R.id.question2);
        final TextView question3 = (TextView) findViewById(R.id.question3);

        //Answer Fields
        final EditText answer1   = (EditText) findViewById(R.id.answer1);
        final EditText answer2   = (EditText) findViewById(R.id.answer2);
        final EditText answer3   = (EditText) findViewById(R.id.answer3);

        //Buttons
        final Button submitButton = (Button) findViewById(R.id.submit);

        //Get data from the server
        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/course-quiz-demo")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {

                    //Query model
                    qa.setRawData(data.toString());
                    final ArrayList<String> questions = qa.getQuestions();
                    final ArrayList<String> answers   = qa.getAnswers();

                    //Populate View
                    question1.setText(questions.get(0));
                    question2.setText(questions.get(1));
                    question3.setText(questions.get(2));

                    //Populate the array of answer fields with the actual answer fields
                    final ArrayList<EditText> studentAnswers = new ArrayList<EditText>();
                    studentAnswers.add(answer1);
                    studentAnswers.add(answer2);
                    studentAnswers.add(answer3);

                    //Submit button behaviour
                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Determine if the quiz has been successful submitted to the server
                            boolean success = submitButtonPressed(studentAnswers, answers);

                            //If so, disable all interactive elements
                            if(success){
                                answer1.setEnabled(false);
                                answer2.setEnabled(false);
                                answer3.setEnabled(false);
                                submitButton.setEnabled(false);
                            }
                        }
                    });
                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * This method checks the student's answers and submits the grades to the server.
     * @param studentAnswers    The student's answers
     * @param answers           The quiz answers
     * @return                  Success status
     */
    private boolean submitButtonPressed(ArrayList<EditText> studentAnswers, ArrayList<String> answers){

       //Extract the student's answers from the text fields
        String[] studentAnswersText = extractStudentAnswers(studentAnswers);

        //Count the number of answers the student got correct
        int numberCorrect = 0;
        for(int i = 0; i < studentAnswersText.length; i++){
            if(studentAnswersText[i].equalsIgnoreCase(answers.get(i))){
                numberCorrect++;
            }
        }

        //Attempt to submit the grade to the server and return success status
        return submitGrade(((double)numberCorrect/studentAnswersText.length)*100);
    }

    /**
     * This method submits the student's grade to the server
     * @param grade     The student's grade
     * @return          Success status
     */
    private boolean submitGrade(double grade) {

        //Package the grade data
        JSONObject data;
        try {
            JSONObject payload = new JSONObject()
                    .put("quiz_name", "Quiz1")
                    .put("grades", String.format("%s", grade));

            data = new JSONObject()
                    .put("url", "http://erudite.ml/course-quiz-submit")
                    .put("auth_token", DataStore.load(R.string.pref_key_token))
                    .put("payload", payload);
        } catch (JSONException je) {
            je.printStackTrace();
            return false;
        }

        //Submit the quiz data to the server
        new FetchAPIData() {
            @Override
            protected void onFetch(JSONObject data) { }
        }.fetch(data);

        //If successful, display message to the user, and return true indicating success
        Toast.makeText(getApplicationContext(),"You submitted the quiz.", Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * This method extracts the student's answers from the text fields
     * @param studentAnswers    The student's answers within the text field
     * @return                  The student's answers
     */
    private String[] extractStudentAnswers(ArrayList<EditText> studentAnswers){
        String[] answers = new String[studentAnswers.size()];
        for(int i = 0; i < answers.length; i++){
            answers[i] = studentAnswers.get(i).getText().toString();
        }
        return answers;
    }

 }
