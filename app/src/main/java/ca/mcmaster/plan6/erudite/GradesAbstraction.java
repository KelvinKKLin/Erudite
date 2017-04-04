package ca.mcmaster.plan6.erudite;

/**
 * Created by Puru on 2017-04-03.
 */

public class GradesAbstraction {


    private String rawData;

    private String success;
    private String message;
    private String userID;
    private String email;
    private String password;
    private String account_type;
    private String courses;


    public GradesAbstraction(String rawData){
        this.rawData = rawData;
    }

    public GradesAbstraction(){
        this.rawData = "";
    }

    private void extractData(){

    }


}
