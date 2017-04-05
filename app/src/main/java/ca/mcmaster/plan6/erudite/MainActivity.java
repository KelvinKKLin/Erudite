package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private static final int SPLASH_REQUEST_CODE = 1;
    private static final int LOGIN_REQUEST_CODE = 2;

    private Button contentButton,
                   quizzesButton,
                   gradesButton,
                   viewerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentButton = (Button) findViewById(R.id.content_button);
        quizzesButton = (Button) findViewById(R.id.quizzes_button);
        gradesButton = (Button) findViewById(R.id.grades_button);
        viewerButton = (Button) findViewById(R.id.viewer_button);

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

        viewerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewerButtonClick();
            }
        });

        //launchSplashActivity();   // Disabled for debugging during development.
        launchLoginActivity();
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

    private void onViewerButtonClick() {
        Intent intent = new Intent(this, ViewerActivity.class);
        startActivity(intent);
    }

}
