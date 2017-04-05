package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Terrance on 2017-04-04.
 */

public class ContentSubmitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentsubmit_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final TextView textView = (TextView) findViewById(R.id.textView);

        textView.setText("testing");

    }
}
