package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    /**
     * Display splash screen animation.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        // Logo images.
        final ImageView logo = (ImageView) findViewById(R.id.logo);
        final ImageView wordmark = (ImageView) findViewById(R.id.wordmark);
        logo.setImageResource(R.drawable.logo);
        wordmark.setImageResource(R.drawable.wordmark);

        // Display animation.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
                logo.startAnimation(anim);
            }
        }, 1000);

        // Finish
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        }, 4000);
    }

    /**
     * When back button is pressed, set result and finish.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result); // Signal the activity was cancelled.
        finish();
    }
}
