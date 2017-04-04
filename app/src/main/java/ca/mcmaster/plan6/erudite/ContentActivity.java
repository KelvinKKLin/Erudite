package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class ContentActivity extends Activity {
    TextView textView;

    ArrayList<Content> content;
    String courseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        textView = (TextView) findViewById(R.id.textView);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //getting courseId
        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/course-list")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    try {
                        courseId = (String) data.getJSONArray("courses").getJSONObject(0).get("_id");
                        DataStore.store(R.string.course_id,courseId);
                    } catch (JSONException je) {
                        je.printStackTrace();
                        return;
                    }
                    //textView.setText(courseId);
                    getCourseContent();
                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }

    }

    private void getCourseContent() {
        //getting content list
        try {
            JSONObject body = new JSONObject()
                    .put("course_id",courseId);

            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/course-content")
                    .put("auth_token", DataStore.load(R.string.pref_key_token))
                    .put("payload",body);

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    try {
                        DataStore.store(R.string.course_content,data.getJSONArray("content_list").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                 renderContent();
                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private void renderContent() {
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        // Initialize contacts
        content = Content.createContactsList();
        // Create adapter passing in the sample user data
        ContentAdapter adapter = new ContentAdapter(this, content);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

}
