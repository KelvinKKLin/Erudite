package ca.mcmaster.plan6.erudite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;


import ca.mcmaster.plan6.erudite.fetch.FetchAPIFile;

public class ViewerActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    private String fileId;
    PdfRenderer pdfRenderer;
    ParcelFileDescriptor fileDescriptor;
    PdfRenderer.Page currentPage;
    ImageView imageView;
    String fileName;
    int pageNumber = 0;


   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        pdfRenderer.close();
        try {
            fileDescriptor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        currentPage.close();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
       // imageView = (ImageView) findViewById(R.id.imageView);

        String courseId = DataStore.load(R.string.course_id);
        JSONArray courseFiles;

        try {
            courseFiles = new JSONArray(DataStore.load(R.string.course_content));
            fileId = courseFiles.getJSONObject(0).getString("file_id");
        } catch (JSONException je) {
            je.printStackTrace();
            return;
        }

        getFile(courseId, fileId);

       /* String TAG = MainActivity.class.getSimpleName();
        int REQUEST_CODE = 42;
//        String SAMPLE_FILE = "zan.pdf";
        PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
        Integer pageNumber = 0;
        String pdfFileName;
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("application/pdf");
        //startActivityForResult(intent, REQUEST_CODE);

        pdfView.fromFile(getFilenow()).load();*/

    }

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

    private File getFilenow(){
        File file = new File(this.getFilesDir(), "abc.pdf");
        return file;
    }

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
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }


/*    private void renderPDF() {
        File file = new File(this.getFilesDir(), "abc.pdf");
        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
            return;
        }

        //showPage(1);

    }

    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        imageView.setImageBitmap(bitmap);
    }*/
}
