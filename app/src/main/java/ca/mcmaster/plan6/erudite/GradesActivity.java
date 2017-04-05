package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class GradesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final ListView listView = (ListView) findViewById(R.id.gradesList);
        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/dash")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                   // textView.setText(data.toString());
                    GradesAbstraction ga = new GradesAbstraction(data.toString());

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ga.getGrades());
                    listView.setAdapter(adapter);

                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }
}
