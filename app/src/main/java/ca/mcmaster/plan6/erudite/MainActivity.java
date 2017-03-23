package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends Activity {

    private static final int SPLASH_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchSplashActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

}
