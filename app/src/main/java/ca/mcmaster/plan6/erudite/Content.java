package ca.mcmaster.plan6.erudite;

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



    public static ArrayList<Content> createContactsList(int numContacts) {
        ArrayList<Content> contacts = new ArrayList<Content>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Content("Assignment 1"));
        }

        return contacts;
    }
}
