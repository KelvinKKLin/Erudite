package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class ContentActivity extends Activity {
    TextView textView;

    ArrayList<Content> content;
    String courseId = "";

    static int buttonPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get data from API.
        try {
            JSONObject data = new JSONObject()
                    .put("url", "http://erudite.ml/course-list")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));

            new FetchAPIData() {
                @Override
                protected void onFetch(JSONObject data) {
                    ContentAbstraction ca = new ContentAbstraction(data);
                    DataStore.store(R.string.course_id,ca.getCourseId());
                    courseId = ca.getCourseId();
                    getCourseContent();
                }
            }.fetch(data);
        } catch (JSONException je) {
            je.printStackTrace();
        }

    }

    private void getCourseContent() {
        // Get course content from API
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

                    // Render content only after all data has been received.
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
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.header);

        // Initialize contacts
        content = Content.createContactsList();
        // Create adapter passing in the sample user data
        ContentAdapter adapter = new ContentAdapter(this, content);
        // Button click listeners
        ContentAdapter mAdapter = new ContentAdapter(content, new ContentAdapter.AdapterListener(){
            @Override
            public void viewButtonOnClick(View v,int position){
                setButtonPos(position);
                onViewButtonClick();
            }
            @Override
            public void submitButtonOnClick(View v,int position){
                setButtonPos(position);
                onSubmitButtonClick();
            }
        });
        rvContacts.setAdapter(mAdapter);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        header.attachTo(rvContacts);
    }

    private void onViewButtonClick() {
        Intent intent = new Intent(this, ContentViewActivity.class);
        startActivity(intent);
    }

    private void onSubmitButtonClick() {
        Intent intent = new Intent(this, ContentSubmitActivity.class);
        startActivity(intent);
    }

    public static void setButtonPos(int position) {
        buttonPos = position;
    }

    public static int getButtonPos() {
        return buttonPos;
    }
}
