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

/**
 * Wrapper for API data request and networking.
 */
public abstract class FetchAPIData extends AsyncTask<String, Void, String> {

    // HTTP request constants.
    protected final String CONTENT_TYPE = "application/x-www-form-urlencoded",
                           HTTP_METHOD = "POST";

    /**
     * Fetch data from an API.
     * @param data HTTP request data.
     */
    public void fetch(JSONObject data) {
        this.execute(data.toString());
    }

    @Override
    protected void onPostExecute(String jsonData) {
        try {
            // Launch custom method to handle response data.
            onFetch(new JSONObject(jsonData));
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    protected abstract void onFetch(JSONObject response);


    @Override
    protected String doInBackground(String... strings) {
        // Convert JSON string into FetchRequest object.
        FetchRequest fetchRequest = parseRequestData(strings[0]);
        // Launch custom method to fetch data.
        return fetchData(fetchRequest);
    }

    /**
     * Send HTTTP request and returns response data as a string.
     * @param fetchRequest
     * @return Response data from API (JSON string).
     */
    protected String fetchData(FetchRequest fetchRequest) {
        StringBuilder builder = new StringBuilder();

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(fetchRequest.getUrl());
            // Setup urlConnection with headers and parameters.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_METHOD);
            urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE);

            // If authentication token provided.
            if (fetchRequest.getAuthToken() != null) {
                urlConnection.setRequestProperty("Authentication", fetchRequest.getAuthToken());
            }

            // Write payload (body) to request.
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
            writer.write(fetchRequest.getPayload());
            writer.close();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            // Receive data from server.
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

    /**
     * Parse JSON string and convert into a FetchRequest object.
     * @param data JSON string of HTTP request data.
     * @return FetchRequest.
     */
    protected FetchRequest parseRequestData(String data) {
        try {
            JSONObject jsonData  = new JSONObject(data);

            String url  = (String) jsonData.get("url");
            String auth_token = null;
            JSONObject payload = null;

            if (jsonData.has("auth_token")) {
                auth_token = (String) jsonData.get("auth_token");
            }

            if (jsonData.has("payload")) {
                payload = (JSONObject) jsonData.get("payload");
            }

            return new FetchRequest(url, auth_token, generatePayload(payload));
        } catch (JSONException je) {
            je.printStackTrace();
            return null;    // FIXME: Throw some kind of exception.
        }
    }

    /**
     * Create x-www-form-urlencoded string from payload.
     * @param json_object
     * @return x-www-form-urlencoded string.
     */
    protected String generatePayload(JSONObject json_object) {
        if (json_object == null) {
            return "";
        }

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
