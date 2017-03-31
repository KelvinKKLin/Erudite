package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class ContentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final TextView textView = (TextView) findViewById(R.id.textView);

        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/course-list")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    textView.setText(data.toString());
                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

}
