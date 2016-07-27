package br.unisinos.kickoffapp.utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import br.unisinos.kickoffapp.models.Enterprise;
import br.unisinos.kickoffapp.models.Player;

/**
 * Created by dennerevaldtmachado on 01/07/16.
 */
public class AuthHttp {
    public static final String AUTH_URL_JSON = "/token";
    public static final String AUTH_URL_FACE_JSON = "/token-facebook";
    public static final String DATA_USER_URL_JSON = "/user";

    public static Boolean login(Context context, String parameters) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(AUTH_URL_JSON, "POST", true, true, null);

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(parameters.getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            String token = new JSONObject(ConnectionUtil.bytesForString(inputStream)).getString("token");
            JSONObject jsonUser = getUserData(token);

            // Save token
            UserPreferences.setToken(context, token);

            if (jsonUser.getJSONObject("Person").get("typeperson").equals("P")) {
                Player player = PlayerHttp.readPlayerObject(jsonUser);
                UserPreferences.setUser(context, null, player);
                return true;
            } else if (jsonUser.getJSONObject("Person").get("typeperson").equals("E")) {
                Enterprise enterprise = EnterpriseHttp.readEnterpriseObject(jsonUser);
                UserPreferences.setUser(context, enterprise, null);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static Boolean loginFacebook(Context context, String parameters) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(AUTH_URL_FACE_JSON, "POST", true, true, null);

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(parameters.getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            String token = new JSONObject(ConnectionUtil.bytesForString(inputStream)).getString("token");
            JSONObject jsonUser = getUserData(token);

            // Save token
            UserPreferences.setToken(context, token);
            if (jsonUser.getJSONObject("Person").get("typeperson").equals("P")) {
                Player player = PlayerHttp.readPlayerObject(jsonUser);
                UserPreferences.setUser(context, null, player);
                return true;
            } else if (jsonUser.getJSONObject("Person").get("typeperson").equals("E")) {
                Enterprise enterprise = EnterpriseHttp.readEnterpriseObject(jsonUser);
                UserPreferences.setUser(context, enterprise, null);
                return true;
            } else {
                return false;
            }
        } else  if(responseServer == HttpURLConnection.HTTP_UNAUTHORIZED) {
            throw new Exception("401");
        } else if (responseServer == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new Exception("404");
        }
        return false;
    }

    private static JSONObject getUserData(String token) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(DATA_USER_URL_JSON, "GET", true, false, token);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            return new JSONObject(ConnectionUtil.bytesForString(inputStream));
        }
        return null;
    }


}
