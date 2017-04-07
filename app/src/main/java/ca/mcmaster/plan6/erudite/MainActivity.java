package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

/**
 * Created by Varun on 2014-04-01.
 * Modified by Kelvin on 2017-04-06.
 */

public class MainActivity extends Activity {

    /**
     * The splash screen request code
     */
    private static final int SPLASH_REQUEST_CODE = 1;

    /**
     * The login request code
     */
    private static final int LOGIN_REQUEST_CODE = 2;

    /**
     * The content button
     */
    private Button contentButton;

    /**
     * The quiz button
     */
    private Button quizzesButton;

    /**
     * The grades button
     */
    private Button gradesButton;

    /**
     * This method defines the initialization behaviour of the Activity
     * @param savedInstanceState       The current instance of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view
        setContentView(R.layout.activity_main);

        //Create references to the button
        contentButton = (Button) findViewById(R.id.content_button);
        quizzesButton = (Button) findViewById(R.id.quizzes_button);
        gradesButton = (Button) findViewById(R.id.grades_button);

        //Create click listeners for the buttons
        contentButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onContentButtonClick();
             }
        });
        quizzesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onQuizzesButtonClick();
            }
        });
        gradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGradesButtonClick();
            }
        });

        //Display the splash screen
        //launchSplashActivity();   // Disabled for debugging during development.

        //Launch the login activity
        launchLoginActivity();
    }

    /**
     * This method defines the behaviour of the activity upon termination of the login activity
     * @param requestCode   The request code
     * @param resultCode    The result code
     * @param data          The next state intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Query the server for account information
        try{
            JSONObject jsonobject = new JSONObject()
                    .put("url", "http://erudite.ml/dash")
                    .put("auth_token", DataStore.load(R.string.pref_key_token));
            new FetchAPIData(){
                @Override
                protected void onFetch(JSONObject jsonobject){
                    //Store the account type into the DataStore
                    MainAbstraction ma = new MainAbstraction(jsonobject.toString());
                    DataStore.store(R.string.account_type, ma.getAccountType());

                    //If the user is a teacher, disable the quiz button and hide it
                    if(ma.getAccountType().equals("Teacher")){
                        quizzesButton.setEnabled(false);
                        quizzesButton.setVisibility(View.INVISIBLE);
                    }

                }
            }.fetch(jsonobject);
        } catch(JSONException e){
            e.printStackTrace();
        }

        //A state machine to determine the behaviour of the code
        switch(requestCode) {
            case SPLASH_REQUEST_CODE:
                if (resultCode == RESULT_CANCELED) {
                    finish();
                } else {
                    launchLoginActivity();
                }
                break;
            case LOGIN_REQUEST_CODE:
                if (resultCode == RESULT_CANCELED) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * This method launches the splash screen
     */
    private void launchSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent, SPLASH_REQUEST_CODE);
    }

    /**
     * This method launches the login activity
     */
    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    /**
     * This method launches the content activity
     */
    private void onContentButtonClick() {
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    /**
     * This method launches the quiz activity
     */
    private void onQuizzesButtonClick() {
        Intent intent = new Intent(this, QuizzesActivity.class);
        startActivity(intent);
    }

    /**
     * This method launches the grades activity
     */
    private void onGradesButtonClick() {
        Intent intent = new Intent(this, GradesActivity.class);
        startActivity(intent);
    }

}
