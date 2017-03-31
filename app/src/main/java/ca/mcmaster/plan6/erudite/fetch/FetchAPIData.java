package ca.mcmaster.plan6.erudite.fetch;

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
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FetchAPIData extends AsyncTask<String, Void, String> {

    protected final String CONTENT_TYPE = "application/x-www-form-urlencoded",
                           HTTP_METHOD = "POST";

    public void fetch(String url, JSONObject data) {
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

    protected abstract void onFetch(JSONObject data);

    @Override
    protected String doInBackground(String... strings) {
        FetchRequest fetchRequest = parseRequestData(strings);
        return fetch(fetchRequest);
    }

    protected String fetch(FetchRequest fetchRequest) {
        StringBuilder builder = new StringBuilder();

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

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
        } catch (Exception e) {
            e.printStackTrace();    // FIXME: Throw some exception.
        } finally {
            urlConnection.disconnect();
        }

        return builder.toString();
    }

    protected FetchRequest parseRequestData(String[] strings) {
        String url = strings[0];
        JSONObject params;

        try {
            params = new JSONObject(strings[1]);
        } catch (JSONException je) {
            je.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        }

        return new FetchRequest(url, generatePayload(params));
    }

    protected String generatePayload(JSONObject json_object) {
        StringBuilder payload = new StringBuilder();
        ArrayList<String> params = new ArrayList<>();

        Iterator<String> keys = json_object.keys();
        while (keys.hasNext()) {
            params.add(keys.next());
        }

        try {
            for (String p : params) {
                payload.append(p
                             + "="
                             + URLEncoder.encode((String) json_object.get(p), "UTF-8")
                             + "&");
            }
        } catch (JSONException je) {
            je.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        }

        payload.deleteCharAt(payload.length() - 1);    // Removed last "&".

        return payload.toString();
    }

}
