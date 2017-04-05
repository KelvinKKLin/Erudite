package ca.mcmaster.plan6.erudite.fetch;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ca.mcmaster.plan6.erudite.EruditeApplication;

public abstract class FetchAPIFile extends FetchAPIData {
    @Override
    protected void onPostExecute(String fileData) {
        onFetch(fileData);
    }

    protected void onFetch(JSONObject jsonData) { }

    protected abstract void onFetch(String fileData);

    @Override
    protected String doInBackground(String... strings) {
        FetchRequest fetchRequest = parseRequestData(strings[0]);
        fetchFile(fetchRequest);
        return "";
    }

    protected void fetchFile(FetchRequest fetchRequest) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(fetchRequest.getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_METHOD);
            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE);

            if (fetchRequest.getAuthToken() != null) {
                urlConnection.setRequestProperty("Authentication", fetchRequest.getAuthToken());
            }

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            writer.write(fetchRequest.getPayload());
            writer.close();

            getResponse(urlConnection);
        } catch (Exception e) {
            e.printStackTrace();    // FIXME: Throw some exception.
        } finally {
            urlConnection.disconnect();
        }
    }

    protected void getResponse(HttpURLConnection urlConnection) throws IOException {
        File f = new File(EruditeApplication.getContext().getFilesDir(), "abc.pdf");

        InputStream input = urlConnection.getInputStream();

        byte[] buffer = new byte[4096];
        int n;

        OutputStream output = new FileOutputStream( f );
        while ((n = input.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        output.close();
    }
}
