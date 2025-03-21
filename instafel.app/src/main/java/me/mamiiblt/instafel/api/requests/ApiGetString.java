package me.mamiiblt.instafel.api.requests;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.mamiiblt.instafel.api.models.InstafelResponse;

public class ApiGetString extends AsyncTask<String, Void, String> {

    private Activity activity;
    public ApiCallbackInterface apiCallbackInterface;
    public int taskId;

    public ApiGetString(Activity activity, ApiCallbackInterface apiCallbackInterface, int taskId) {
        this.apiCallbackInterface = apiCallbackInterface;
        this.taskId = taskId;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... f_url) {
        String responseString = null;
        try {
            URL url = new URL(f_url[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                responseString = response.toString();
            } else {
                Log.v("Instafel", "Request failed with code " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String responseString) {
        if (apiCallbackInterface != null) {
            apiCallbackInterface.getResponse(responseString, taskId);
        }
    }
}
