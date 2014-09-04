package cn.weathermodule1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class GetWeatherTask extends AsyncTask<URL, Integer, ArrayList<Weather>> {

    private static final String TAG = "GetWeatherTask";
    Context mainContext;
    URL webServiceUrl;
    String jsonString;


    GetWeatherTask(Context context) {
        mainContext = context;
    }

    @Override
    protected ArrayList<Weather> doInBackground(URL... urls) {
        webServiceUrl = urls[0];
        jsonString = connectToServiceAndGetJsonString(webServiceUrl);
        ArrayList<Weather> list = jsonStringDeserialize();

        return list;
    }

    private String connectToServiceAndGetJsonString(URL webServiceUrl) {
        BufferedReader inStrmReader = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) webServiceUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            inStrmReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder fromJsnStrngBldr = new StringBuilder();
            String line;
            while ((line = inStrmReader.readLine()) != null) {
                fromJsnStrngBldr.append(line);
            }

            jsonString = fromJsnStrngBldr.toString();

        } catch (IOException e) {
            Log.w(TAG, "Error while receiving data from server", e);
        } finally {
            try {
                if (inStrmReader != null) {
                    inStrmReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                Log.w(TAG, "Error while closing reader or connection", e);
            }
        }
        Log.i(TAG, " jsonString: " + jsonString);
        return jsonString;
    }

    private ArrayList<Weather> jsonStringDeserialize() {
        GsonBuilder gsnBldr = new GsonBuilder();
        gsnBldr.registerTypeAdapter(Weather[].class, new WeatherDeserializer());
        Gson gson = gsnBldr.create();
        Weather[] array = gson.fromJson(jsonString, Weather[].class);
        return new ArrayList<Weather>(Arrays.asList(array));
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> list) {
        ShowResult.refreshView(list);
    }
}
