package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
            String url = "http://erudite.ml/login";
            JSONObject data = new JSONObject()
                    .put("email", "test@test.com")
                    .put("password", "password");

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    textView.setText(data.toString());
                }
            }.fetch(url, data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

}
