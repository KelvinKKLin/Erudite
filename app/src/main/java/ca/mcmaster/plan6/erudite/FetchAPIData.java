package ca.mcmaster.plan6.erudite;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// TODO: Make abstract.
public class FetchAPIData extends AsyncTask<String, Void, String> {

    protected final String CONTENT_TYPE = "application/x-www-form-urlencoded",
                           HTTP_METHOD = "POST";

    protected void fetch(String url, JSONObject data) {
        this.execute(url, data.toString());
    }

    @Override
    protected void onPostExecute(String data) {
        try {
            onFetch(new JSONObject(data));
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    protected void onFetch(JSONObject data) {
        return;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = null;

        FetchRequest fetchRequest = parseRequestData(strings);

        return fetch(fetchRequest);
    }

    protected String fetch(FetchRequest fetchRequest) {
        String data = null;

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(fetchRequest.getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_METHOD);
            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE);

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            writer.write(fetchRequest.getPayload());
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
            e.printStackTrace();    // FIXME: Throw some exception.
        } finally {
            urlConnection.disconnect();
        }

        return data;

    }

    protected FetchRequest parseRequestData(String[] strings) {
        String url = strings[0];

        String payload_template = "email=%s&password=%s",
                payload;

        JSONObject params;

        String email,
               password;

        try {
            params = new JSONObject(strings[1]);
            email = params.getString("email");
            password = params.getString("password");

            payload = String.format(payload_template,
                URLEncoder.encode(email, "UTF-8"),
                URLEncoder.encode(password, "UTF-8"));
        } catch (JSONException je) {
            je.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        }

        return new FetchRequest(url, payload);
    }

}
