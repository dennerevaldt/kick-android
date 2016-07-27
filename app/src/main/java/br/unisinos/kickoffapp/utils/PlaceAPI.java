package br.unisinos.kickoffapp.utils;

import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import br.unisinos.kickoffapp.models.Place;

/**
 * Created by dennerevaldtmachado on 04/07/16.
 */
public class PlaceAPI {
    private static final String TAG = PlaceAPI.class.getSimpleName();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyD6bzbwKaZKiHy12YpFQr7D-B4ETA5noKo";

    public ArrayList<Place> autocomplete (String input) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            sb.append("&language=pt_BR");
            sb.append("&components=country:br");

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new Place(predsJsonArray.getJSONObject(i).getString("place_id"), predsJsonArray.getJSONObject(i).getString("description")));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public LatLng getLatLongLocale(String placeId) {
        LatLng latLng = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_DETAILS + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&placeid=" + placeId);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return latLng;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return latLng;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonLocationLatLng = new JSONObject(jsonResults.toString()).getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
            latLng = new LatLng(jsonLocationLatLng.getDouble("lat"), jsonLocationLatLng.getDouble("lng"));

        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return latLng;
    }
}
