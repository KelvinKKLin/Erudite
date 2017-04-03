package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class ContentActivity extends Activity {
    ArrayList<Content> content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        content = Content.createContactsList(1);
        // Create adapter passing in the sample user data
        ContentAdapter adapter = new ContentAdapter(this, content);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

 /*   @Override
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
    }*/

}
