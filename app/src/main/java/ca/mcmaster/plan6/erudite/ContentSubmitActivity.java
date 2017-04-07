package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContentSubmitActivity extends Activity {
    private final int CHOOSE_FILE_REQUESTCODE = 1;

    private Button chooseButton;
    private Button uploadButton;
    private TextView textView;

    private boolean fileSelected = false;
    private Uri fileData;

    JSONArray courses, courseFiles;
    private String course_id, file_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentsubmit_activity);

        chooseButton = (Button) findViewById(R.id.choose_button);
        uploadButton = (Button) findViewById(R.id.upload_button);
        textView = (TextView) findViewById(R.id.textView);

        uploadButton.setEnabled(false);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile("application/pdf");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileSelected) {
                    Log.i("erudite", "onClick: UPLOAD");
                    sendFile();
                }
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
                    fileSelected = true;
                    fileData = data.getData();
                    textView.setText(data.getDataString());
                    uploadButton.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }


    private void sendFile() {
        ContentAbstraction ca = new ContentAbstraction();
        course_id = ca.getCourseID();
        file_id = ca.getFileId();

        /* Unable to get full file path from Uri (content://)
           Instead copy the file to known file directory. */
        copyFileToFilesDir(fileData);

        Ion.with(this)
                .load("http://erudite.ml/course-content-submit/" + course_id + "/" + file_id)
                .setHeader("authentication", DataStore.load(R.string.pref_key_token))
                .setMultipartFile("assignment", new File(this.getFilesDir() + "/tmp"))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.i("erudite", "onCompleted:" + ((result == null) ? " null" : result.toString()));
                        if (result != null) {
                            Toast.makeText(EruditeApplication.getContext(), "Uploaded :)", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(EruditeApplication.getContext(), "Oh no, upload failed :(", Toast.LENGTH_LONG)
                                    .show();
                        }

                        finish();   // Return to previous activity after submitting.
                    }
                });

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

    public void copyFileToFilesDir(Uri uri) {

        try {
            File fout = new File(EruditeApplication.getContext().getFilesDir(), "tmp");

            InputStream input = getContentResolver().openInputStream(uri);
            FileOutputStream output = new FileOutputStream(fout);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }

            output.close();
            input.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }

}