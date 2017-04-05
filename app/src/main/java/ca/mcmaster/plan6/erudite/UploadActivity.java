package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class UploadActivity extends Activity {
    private final int CHOOSE_FILE_REQUESTCODE = 1;

    private Button chooseButton;
    private Button uploadButton;
    private TextView textView;

    private boolean fileSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        chooseButton = (Button) findViewById(R.id.choose_button);
        uploadButton = (Button) findViewById(R.id.upload_button);
        textView = (TextView) findViewById(R.id.textView);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile("application/pdf");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileSelected)
                    Log.i("erudite", "onClick: UPLOAD");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                if (resultCode == RESULT_CANCELED) {
                    finish();
                    Log.i("erudite", "onActivityResult: REQUEST_CANCELED");
                } else {
                    Log.i("erudite", "onActivityResult: REQUEST_OK");
                }
                fileSelected = true;
                textView.setText(data.getDataString());
                break;
            default:
                break;
        }
    }

    /**
     * Copyright: stackoverflow.com/Chupik and stackoverflow.com/Vitaly-Zinchenko
     */
    public void openFile(String minmeType) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(minmeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Special intent for Samsung file manager.
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
         // If you want any file type, you can skip next line.
        sIntent.putExtra("CONTENT_TYPE", minmeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with samsung file manager.
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        }
        else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }
}
