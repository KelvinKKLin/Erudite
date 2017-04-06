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
import ca.mcmaster.plan6.erudite.fetch.StatsPackage;

public class GradesActivity extends Activity {

    private boolean hasLoadedStats = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_activity_teacher);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //final ImageView aa = (ImageView) findViewById(R.id.gradeImages);

        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/dash")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    //Format server data
                    GradesAbstraction ga = new GradesAbstraction(data.toString());

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

    private void populateStudentView(final GradesAbstraction ga){
        StatsPackage statsPackage = new StatsPackage();
        double mean = statsPackage.computeMean(ga.getGradeValues());

        setContentView(R.layout.grades_activity_student);
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

        gradeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.grades_activity_teacher);
                populateTeacherView(ga);
            }
        });
    }

    private void populateTeacherView(final GradesAbstraction ga){

        //Variable Declarations
        Button switchViewButton = (Button) findViewById(R.id.switchViewButton);
        final ListView listView = (ListView) findViewById(R.id.gradesList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);

        //Variable Configuration
        listView.setAdapter(adapter);

        for(String s : ga.getGrades()){
            adapter.add(s);
        }

        //Compute Statistics
        StatsPackage statsPackage = new StatsPackage();
        adapter.add("Mode: " + statsPackage.computeMean(ga.getGradeValues()));
        adapter.add("Median: " + statsPackage.computeMedian(ga.getGradeValues()));
        adapter.add("Mode: " + statsPackage.computeMode(ga.getGradeValues()));
        adapter.add("Variance: " + statsPackage.computeVariance(ga.getGradeValues()));
        adapter.add("Standard Deviation: " + statsPackage.stdDeviation(ga.getGradeValues()));

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
