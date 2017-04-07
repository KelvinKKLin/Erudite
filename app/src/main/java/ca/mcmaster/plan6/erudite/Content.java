package ca.mcmaster.plan6.erudite;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Terrance on 2017-04-02.
 */

public class Content {
    private String docName;

    public Content(String name) {
        docName = name;
    }

    public String getName() {
        return docName;
    }

    /**
     * creates the list of content that will be displayed on the content view
     */
    public static ArrayList<Content> createContactsList() {
        ArrayList<Content> contacts = new ArrayList<Content>();
        try {
            JSONArray numContent = new JSONArray(DataStore.load(R.string.course_content));
            for (int i = 0; i <= numContent.length()-1; i++) {
                contacts.add(new Content(numContent.getJSONObject(i).get("name").toString()));
            }
            return contacts;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
