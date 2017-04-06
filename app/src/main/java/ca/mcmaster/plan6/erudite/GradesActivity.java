package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;
import ca.mcmaster.plan6.erudite.fetch.StatsPackage;

public class GradesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_activity_teacher);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView listView = (ListView) findViewById(R.id.gradesList);
        final ImageView gradeImage = (ImageView) findViewById(R.id.gradeImage);

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
                        StatsPackage statsPackage = new StatsPackage();
                        double mean = statsPackage.computeMean(ga.getGradeValues());

                        setContentView(R.layout.grades_activity_student);
                        if(mean >= 80){
                            //Display 'a' grade picture
                            gradeImage.setImageResource(R.drawable.a);
                        } else if(mean >= 70){
                            //Display 'b' grade picture
                            gradeImage.setImageResource(R.drawable.b);
                        } else if(mean >= 60){
                            //Display 'c' grade picture
                            gradeImage.setImageResource(R.drawable.c);
                        } else if(mean >= 50){
                            //Display 'd' grade picture
                            gradeImage.setImageResource(R.drawable.d);
                        } else{
                            //Display 'F' grade picture
                            gradeImage.setImageResource(R.drawable.fail);
                        }

                    } else {
                        //Display Grades
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, ga.getGrades());
                        listView.setAdapter(adapter);

                        //Compute Statistics
                        StatsPackage statsPackage = new StatsPackage();
                        adapter.add("Mode: " + statsPackage.computeMean(ga.getGradeValues()));
                        adapter.add("Median: " + statsPackage.computeMedian(ga.getGradeValues()));
                        adapter.add("Mode: " + statsPackage.computeMode(ga.getGradeValues()));
                        adapter.add("Variance: " + statsPackage.computeVariance(ga.getGradeValues()));
                        adapter.add("Standard Deviation: " + statsPackage.stdDeviation(ga.getGradeValues()));
                    }

                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
