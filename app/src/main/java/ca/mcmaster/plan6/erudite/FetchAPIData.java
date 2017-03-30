package ca.mcmaster.plan6.erudite;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FetchAPIData extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        String data = null;

        String email = "test@test.com",
                password = "password",
                payload_template = "email=%s&password=%s",
                payload = "";

        try {
            payload = String.format(payload_template,
                    URLEncoder.encode(email, "UTF-8"),
                    URLEncoder.encode(password, "UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            Log.e("NETWORK", "doInBackground", uee);
        }

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("http://erudite.ml/login");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }

            data = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return data;
    }
}
