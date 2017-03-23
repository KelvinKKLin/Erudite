package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        final ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(R.drawable.logo);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
                image.startAnimation(anim);
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }
}
