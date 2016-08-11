package br.unisinos.kickoffapp.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Enterprise;

/**
 * Created by dennerevaldtmachado on 21/07/16.
 */
public class CourtHttp {
    private static final String COURT_URL_JSON = "/court";

    /**
     * Create new court
     * @param court
     * @param context
     * @return
     * @throws Exception
     */
    public Court createCourt (Court court, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(COURT_URL_JSON, "POST", true, true, token);

        StringBuilder sb = new StringBuilder();
        sb.append("name=" + court.getName());
        sb.append("&category=" + court.getCategory());

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(sb.toString().getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonCourtObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            return readCourtObject(jsonCourtObject);
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao inserir quadra.");
        }
        return null;
    }

    /**
     * Delete court item
     * @param court
     * @param context
     * @return
     * @throws Exception
     */
    public Boolean deleteCourt (Court court, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        String ID_COURT = "/" + court.getIdCourt();
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(COURT_URL_JSON+ID_COURT, "DELETE", true, true, token);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            return true;
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao excluir quadra.");
        }
        return null;
    }

    /**
     * Update court item
     * @param court
     * @param context
     * @return
     * @throws Exception
     */
    public Court updateCourt (Court court, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        String ID_COURT = "/" + court.getIdCourt();
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(COURT_URL_JSON+ID_COURT, "PUT", true, true, token);

        StringBuilder sb = new StringBuilder();
        sb.append("name=" + court.getName());
        sb.append("&category=" + court.getCategory());

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(sb.toString().getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonCourtObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            return readCourtObject(jsonCourtObject);
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao editar quadra.");
        }
        return null;
    }

    /**
     * Get all courts registers
     * @return List Player
     */
    public static List<Court> getAllCourts(Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection = null;
        int responseServer = 0;
        try {
            httpURLConnection = ConnectionUtil.connect(COURT_URL_JSON, "GET", true, false, token);
            responseServer = httpURLConnection.getResponseCode();

        } catch (Exception e) {
            throw new Exception("Falha na conexão com a API");
        }

        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllCourtsArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Court> courts = readCourtArray(jsonAllCourtsArray);
            return courts;
        }
        return null;
    }

    /**
     * Read json object court
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Court readCourtObject(JSONObject jsonObject) throws JSONException {
        Court court = new Court(
                jsonObject.getString("id"),
                jsonObject.getString("name"),
                jsonObject.getString("category")
        );
        return court;
    }

    /**
     * Read json array court
     * @param jsonCourtArray
     * @return
     * @throws JSONException
     */
    public static List<Court> readCourtArray(JSONArray jsonCourtArray) throws JSONException {
        List<Court> CourtList = new ArrayList<>();

        for (int i = 0; i < jsonCourtArray.length(); i++){
            JSONObject jsonCourt = jsonCourtArray.getJSONObject(i);

            Court Court = new Court(
                    jsonCourt.getString("id"),
                    jsonCourt.getString("name"),
                    jsonCourt.getString("category")
            );
            CourtList.add(Court);
        }

        return CourtList;
    }

}
