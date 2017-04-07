package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;
import ca.mcmaster.plan6.erudite.fetch.StatisticsCalculator;

import static ca.mcmaster.plan6.erudite.R.drawable.a;

/**
 * Created by Varun on 2014-04-01.
 * Modified by Kelvin on 2017-04-03.
 */

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

        //If the account is a student account, load the student grade view
        if(DataStore.load(R.string.account_type).equals("Student")) {

            Log.v("STUDENT", "LOADED");

            //Query the server for user information
            try {
                JSONObject data = new JSONObject()
                        .put("url", "http://erudite.ml/dash")
                        .put("auth_token", DataStore.load(R.string.pref_key_token));

                new FetchAPIData() {
                    @Override
                    protected void onFetch(JSONObject data) {
                        //Format server data
                        SimpleGradesAbstraction ga = new SimpleGradesAbstraction(data.toString());
                        populatePictureView(ga);
                    }
                }.fetch(data);
            } catch (JSONException je) {
                je.printStackTrace();
            }
        } else{
            //Query the server for user information
            try {
                JSONObject data = new JSONObject()
                        .put("url", "http://erudite.ml/dash-teacher")
                        .put("auth_token", DataStore.load(R.string.pref_key_token));

                new FetchAPIData() {
                    @Override
                    protected void onFetch(JSONObject data) {
                        //Format server data
                        ClassGradesAbstraction ga = new ClassGradesAbstraction(data.toString());
                        populateGradesView(ga);
                    }
                }.fetch(data);
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
    }

    /**
     * This method defines the behaviour for the student view.
     * This is our innovative feature.
     * @param ga    GradeAbstraction data
     */
    private void populatePictureView (final SimpleGradesAbstraction ga){

        //Calculate the student's average
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        double mean = statisticsCalculator.computeMean(ga.getGradeValues());

        //Display the student grades view
        setContentView(R.layout.grades_activity_student);

        //Depending on the student's average, display the corresponding feedback image
        ImageButton gradeImage = (ImageButton) findViewById(R.id.gradeImages);
        if(mean >= 80){
            //Display 'a' grade picture
            gradeImage.setBackgroundResource(a);
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
                populateGradesView(ga);
            }
        });
    }

    /**
     * This method defines the behaviour for the numerical grades view.
     * @param ga    SimpleGradeAbstraction data
     */
    private void populateGradesView(final GradesAbstraction ga){

        //Variable Declarations
        Button switchViewButton = (Button) findViewById(R.id.switchViewButton);
        TextView titleText = (TextView) findViewById(R.id.titleText);

        final ListView listView = (ListView) findViewById(R.id.gradesList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);

        //Variable Configuration
        listView.setAdapter(adapter);

       repopulateAdapter(adapter, ga);

        //If the user is a student, define behaviours for the back button
        if(ga instanceof SimpleGradesAbstraction) {
            switchViewButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setContentView(R.layout.grades_activity_student);
                    populatePictureView((SimpleGradesAbstraction) ga);
                }

            });
        } else{ //If the user is a teacher, update the title to show student name
            String title = titleText.getText().toString();
            title += ":\n" + ga.getNames().get(0);
            titleText.setText(title);

            //Redefine the behaviour for the switchViewButton to refresh student grades
            switchViewButton.setText("Refresh");
            switchViewButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    try {
                        JSONObject data = new JSONObject()
                                .put("url", "http://erudite.ml/dash-teacher")
                                .put("auth_token", DataStore.load(R.string.pref_key_token));

                        new FetchAPIData() {
                            @Override
                            protected void onFetch(JSONObject data) {
                                //Format server data
                                ga.setRawData(data.toString());

                                //Clear the adapter
                                adapter.clear();
                                adapter.notifyDataSetChanged();

                                //Repopulate the adapter
                                repopulateAdapter(adapter, ga);
                                adapter.notifyDataSetChanged();
                            }
                        }.fetch(data);
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }

            });

        }
    }

    /**
     * This method repopulates the ArrayAdapter with new grades
     * @param adapter   ArrayAdapter to repopulate
     * @param ga        Updated GradeAbstraction
     */
    private void repopulateAdapter(ArrayAdapter<String> adapter, GradesAbstraction ga){
        //Add the student grades to the list
        for(String s : ga.getGrades()){
            adapter.add(s);
        }

        //Compute Statistics
        computeStatistics(ga, adapter);
    }

    /**
     * This method computes the statistics for the GradeAbstraction object, and sets the
     * appropriate values in the ArrayAdapter
     * @param ga            GradeAbstraction containing grade data
     * @param adapter       ArrayAdapter to display grades
     */
    private void computeStatistics(GradesAbstraction ga, ArrayAdapter<String> adapter){
        StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
        adapter.add("Mean: " + statisticsCalculator.computeMean(ga.getGradeValues()));
        adapter.add("Median: " + statisticsCalculator.computeMedian(ga.getGradeValues()));
        adapter.add("Mode: " + statisticsCalculator.computeMode(ga.getGradeValues()));
        adapter.add("Variance: " + statisticsCalculator.computeVariance(ga.getGradeValues()));
        adapter.add("Standard Deviation: " + statisticsCalculator.stdDeviation(ga.getGradeValues()));
    }
}
