package ca.mcmaster.plan6.erudite.main;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kelvin on 2017-04-07.
 */

public class MainAbstraction {

    /**
     * The unprocessed data
     */
    private String rawData;

    /**
     * The user's account type
     */
    private String accountType;

    /**
     * MainAbstraction constructor without raw data parameter
     */
    public MainAbstraction(){
        this.rawData = "";
    }

    /**
     * MainAbstraction constructor with raw data parameter
     * @param rawData   Unprocessed data
     */
    public MainAbstraction(String rawData){
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method sets the raw data variable
     * @param rawData   Unprocessed data
     */
    public void setRawData(String rawData){
        this.rawData = rawData;
        extractData();
    }

    /**
     * This method processes the raw data and assigns values to the appropriate variables.
     */
    public void extractData(){
        try {
            JSONObject processedData = new JSONObject(this.rawData);
            JSONObject userData = new JSONObject(processedData.getString("user"));
            this.accountType = userData.getString("account_type");
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * This method returns the account type
     * @return  Account type
     */
    public String getAccountType(){
        return this.accountType;
    }

}
