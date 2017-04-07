package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class MainActivity extends Activity {

    private static final int SPLASH_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 2;

    private Button contentButton,
                   quizzesButton,
                   gradesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        contentButton = (Button) findViewById(R.id.content_button);
        quizzesButton = (Button) findViewById(R.id.quizzes_button);
        gradesButton = (Button) findViewById(R.id.grades_button);

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

        //launchSplashActivity();   // Disabled for debugging during development.
        launchLoginActivity();
    }

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
                    try {

                        //Store the account type into the DataStore
                        String rawData = jsonobject.toString();
                        JSONObject processedData = new JSONObject(rawData);
                        JSONObject userData = new JSONObject(processedData.getString("user"));
                        String accountType = userData.getString("account_type");
                        DataStore.store(R.string.account_type,accountType);

                        //If the user is a teacher, disable the quiz button and hide it
                        if(accountType.equals("Teacher")){
                            quizzesButton.setEnabled(false);
                            quizzesButton.setVisibility(View.INVISIBLE);
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }.fetch(jsonobject);
        } catch(JSONException e){
            e.printStackTrace();
        }

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

    private void launchSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent, SPLASH_REQUEST_CODE);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    private void onContentButtonClick() {
        Intent intent = new Intent(this, ContentActivity.class);
        startActivity(intent);
    }

    private void onQuizzesButtonClick() {
        Intent intent = new Intent(this, QuizzesActivity.class);
        startActivity(intent);
    }

    private void onGradesButtonClick() {
        Intent intent = new Intent(this, GradesActivity.class);
        startActivity(intent);
    }

}
