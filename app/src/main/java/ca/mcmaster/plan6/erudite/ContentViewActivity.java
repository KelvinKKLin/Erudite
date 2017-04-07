package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ca.mcmaster.plan6.erudite.fetch.FetchAPIFile;

/**
 * Created by Terrance on 2017-04-04.
 */

public class ContentViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private String fileId;
    String fileName;
    int pageNumber = 0;

    /**
     * This method initializes the Activity
     * @param savedInstanceState    The current state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentview_activity);

        String courseId = DataStore.load(R.string.course_id);
        JSONArray courseFiles;

        ContentAbstraction ca = new ContentAbstraction();
        fileId = ca.getFileId();

        getFile(courseId, fileId);

    }
    /**
     * This method gets the file from the server
     * @param courseId Id of the course where the content is coming from
     * @param fileId Id of the file that is being retrieved
     */
    private void getFile(String courseId, String fileId) {
        JSONObject data;

        try {
            JSONObject payload = new JSONObject()
                    .put("course_id", courseId)
                    .put("file_id", fileId);

            data = new JSONObject()
                    .put("url", "http://erudite.ml/course-content-file")
                    .put("auth_token", DataStore.load(R.string.pref_key_token))
                    .put("payload", payload);
        } catch (JSONException je) {
            je.printStackTrace();
            return;
        }

        new FetchAPIFile() {
            @Override
            protected void onFetch(String response) {
                displayFromFile(getFilenow());
            }
        }.fetch(data);

    }
    /**
     * This method gets the file by the file name from temp storage
     * @return the file
     */
    private File getFilenow(){
        File file = new File(this.getFilesDir(), "abc.pdf");
        return file;
    }
    /**
     * This method opens a pdf viewer and displays the file
     * @param file the file is is to be displayed
     */
    private void displayFromFile(File file){
        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        fileName = file.getName();

        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(false)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override
    /**
     * This method defines the behaviour of the Content View subsystem after initialization
     */
    protected void onStart() {
        super.onStart();

        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.valueOf(ContentActivity.getButtonPos()));


    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
