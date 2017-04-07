package ca.mcmaster.plan6.erudite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Terrance on 2017-04-06.
 */

public class ContentAbstraction {

    private JSONObject rawData;

    private JSONArray courseFiles;

    private String courseId = "";

    private String fileId = "";

    public ContentAbstraction(JSONObject rawData) {
        this.rawData = rawData;
        extractData();
    }

    public ContentAbstraction() {

    }

    private void extractData() {
        try {
            courseId = (String) rawData.getJSONArray("courses").getJSONObject(0).get("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCourseID() {
        JSONArray courses = null;
        try {
            courses = new JSONArray(DataStore.load(R.string.course_id));
            courseId = courses.getJSONObject(ContentActivity.getButtonPos()).getString("course_id");
            return courseId;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getCourseId() {
        return courseId;
    }

    public String getFileId() {
        try {
            courseFiles = new JSONArray(DataStore.load(R.string.course_content));
            fileId = courseFiles.getJSONObject(ContentActivity.getButtonPos()).getString("file_id");
            return fileId;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
