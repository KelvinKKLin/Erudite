package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;
import ca.mcmaster.plan6.erudite.fetch.StatisticsCalculator;

public class GradesActivity extends Activity {

    /**
     * This method initializes the GradesActivity.
     * @param savedInstanceState    The current instance of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_activity_teacher);
    }

    /**
     * This method defines the behaviour of the GradesActivity after initialization
     */
    @Override
    protected void onStart() {
        super.onStart();

        //Query the server for user information
        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/dash")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    //Format server data
                    GradesAbstraction ga = new GradesAbstraction(data.toString());

                    //Decide which view to display depending on the type of account
                    if(ga.getAccountType().equals("Student")){
                        populateStudentView(ga);
                    } else {
                        populateTeacherView(ga);
                    }

                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    /**
     * This method defines the behaviour for the student view.
     * This is our innovative feature.
     * @param ga    GradeAbstraction data
     */
    private void populateStudentView(final GradesAbstraction ga){

        //Calculate the student's average
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        double mean = statisticsCalculator.computeMean(ga.getGradeValues());

        //Display the student grades view
        setContentView(R.layout.grades_activity_student);

        //Depending on the student's average, display the corresponding feedback image
        ImageButton gradeImage = (ImageButton) findViewById(R.id.gradeImages);
        if(mean >= 80){
            //Display 'a' grade picture
            gradeImage.setBackgroundResource(R.drawable.a);
        } else if(mean >= 70){
            //Display 'b' grade picture
            gradeImage.setBackgroundResource(R.drawable.b);
        } else if(mean >= 60){
            //Display 'c' grade picture
            gradeImage.setBackgroundResource(R.drawable.c);
        } else if(mean >= 50){
            //Display 'd' grade picture
            gradeImage.setBackgroundResource(R.drawable.d);
        } else{
            //Display 'F' grade picture
            gradeImage.setBackgroundResource(R.drawable.fail);
        }

        //If the user presses the image, switch to teacher view
        gradeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.grades_activity_teacher);
                populateTeacherView(ga);
            }
        });
    }

    /**
     * This method defines the behaviour for the teacher view.
     * @param ga    GradeAbstraction data
     */
    private void populateTeacherView(final GradesAbstraction ga){

        //Variable Declarations
        Button switchViewButton = (Button) findViewById(R.id.switchViewButton);
        final ListView listView = (ListView) findViewById(R.id.gradesList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);

        //Variable Configuration
        listView.setAdapter(adapter);

        //Add the student grades to the list
        for(String s : ga.getGrades()){
            adapter.add(s);
        }

        //Compute Statistics
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        adapter.add("Mode: " + statisticsCalculator.computeMean(ga.getGradeValues()));
        adapter.add("Median: " + statisticsCalculator.computeMedian(ga.getGradeValues()));
        adapter.add("Mode: " + statisticsCalculator.computeMode(ga.getGradeValues()));
        adapter.add("Variance: " + statisticsCalculator.computeVariance(ga.getGradeValues()));
        adapter.add("Standard Deviation: " + statisticsCalculator.stdDeviation(ga.getGradeValues()));

        //If the user is a student, allow the user to switch between student and teacher views
        //Otherwise, define behaviour for teacher-only features
        if(ga.getAccountType().equals("Student")){
            switchViewButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    setContentView(R.layout.grades_activity_student);
                    populateStudentView(ga);
                }

            });
        } else{
            switchViewButton.setEnabled(false);
            switchViewButton.setVisibility(View.INVISIBLE);
        }

    }
}
