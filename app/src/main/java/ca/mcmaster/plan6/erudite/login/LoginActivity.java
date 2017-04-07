package ca.mcmaster.plan6.erudite.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ca.mcmaster.plan6.erudite.main.DataStore;
import ca.mcmaster.plan6.erudite.R;
import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

/**
 * Created by Varun on 2014-04-01.
 *
 */
public class LoginActivity extends Activity {

    /**
     * The email input box
     */
    private AutoCompleteTextView mEmailView;

    /**
     * The password box
     */
    private EditText mPasswordView;

    /**
     * The progress view
     */
    private View mProgressView;

    /**
     * The form view
     */
    private View mLoginFormView;

    /**
     * This method defines the initialization behaviour of the login activity
     * @param savedInstanceState    The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Define the view
        setContentView(R.layout.login_activity);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * This method attempts to submit the user login credentials to the server.
     */
    private void attemptLogin() {

        //Variable declarations
        String urlLogin = "http://erudite.ml/login";
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //Package the data
        JSONObject data;
        try {
            JSONObject payload = new JSONObject()
                    .put("email", email)
                    .put("password", password);

            data = new JSONObject()
                    .put("url", urlLogin)
                    .put("payload", payload);
        } catch (JSONException je) {
            je.printStackTrace();
            return;
        }

        //Process the response
        new FetchAPIData() {
            @Override
            protected void onFetch(JSONObject response) {
                try {
                    if ((boolean) response.get("success")) {
                        login((String) response.get("token"));
                    } else {
                        loginFailed();
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }.fetch(data);
    }

    /**
     * This method defines the successful login behaviour for the application
     * @param token     The user login token
     */
    private void login(String token) {

        //Store the login token
        DataStore.store(R.string.pref_key_token, token);

        //Return to the main menu
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * This method defines the failed login behaviour for the application.
     * It turns all input fields red.
     */
    private void loginFailed() {
        mEmailView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDanger, null));
        mPasswordView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDanger, null));
    }
}
