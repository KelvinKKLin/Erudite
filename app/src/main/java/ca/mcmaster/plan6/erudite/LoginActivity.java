package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
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

import ca.mcmaster.plan6.erudite.fetch.FetchAPIData;

public class LoginActivity extends Activity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onStart() {
        super.onStart();
        mEmailView.setText("test@test.com");
        mPasswordView.setText("password");
    }

    private void attemptLogin() {
        String urlLogin = "http://erudite.ml/login";

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        JSONObject data = null;
        try {
            data = new JSONObject()
                    .put("email", email)
                    .put("password", password);
        } catch (JSONException je) {
            je.printStackTrace();
            return;
        }

        new FetchAPIData() {
            @Override
            protected void onFetch(JSONObject data) {
                try {
                    if ((boolean) data.get("success")) {
                        login((String) data.get("token"));
                    } else {
                        loginFailed();
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }.fetch(urlLogin, data);
    }

    private void login(String token) {
        storeToken(token);

        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

    private void loginFailed() {
        mEmailView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDanger, null));
        mPasswordView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorDanger, null));
    }

    private void storeToken(String token) {
        SharedPreferences shared_pref = this.getSharedPreferences(
                getString(R.string.pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_pref.edit();
        editor.putString(getString(R.string.pref_key_token), token);
        editor.commit();
    }

}
