package com.example.android.earthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static String makeHttpRequest(URL url) throws IOException {
        String JSONresponse = "";

        if (url == null) return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(50000);
            urlConnection.setConnectTimeout(8000);
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                JSONresponse = readStream(inputStream);
            }
        }catch (IOException e){
            e.getStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JSONresponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() { }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String SAMPLE_JSON_RESPONSE) {

        if (TextUtils.isEmpty(SAMPLE_JSON_RESPONSE)) return null;

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);

            JSONArray listOfEarthquakes = root.optJSONArray("features");

            for(int i=0 ; i<listOfEarthquakes.length() ; i++){

                JSONObject prop = listOfEarthquakes.getJSONObject(i).getJSONObject("properties");

                String location = prop.getString("place");
                String offSet, place;

                if (location.contains("of")) {
                    offSet = location.substring(0, location.indexOf("of") + 2);
                    place = location.substring(location.indexOf("of") + 3, location.length());
                }
                else {
                    offSet = "Near the";
                    place = location;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(prop.getLong("time"));

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY \nHH:mm a", Locale.getDefault());
                String date = dateFormat.format(calendar.getTime());

                earthquakes.add(new Earthquake(prop.getDouble("mag"), offSet, place, date,prop.getString("url")));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}