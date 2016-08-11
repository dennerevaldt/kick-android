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
 * Created by dennerevaldtmachado on 29/06/16.
 */
public class EnterpriseHttp {
    private static final String ENTERPRISE_URL_JSON = "/enterprise";

    /**
     * Create new enterprise
     * @param parameters
     * @return
     * @throws Exception
     */
    public Enterprise createEnterprise (String parameters) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(ENTERPRISE_URL_JSON, "POST", true, true, null);

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(parameters.getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonEnterpriseObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            Enterprise enterprise = readEnterpriseObject(jsonEnterpriseObject);
            return enterprise;
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Empresa já existe");
        }
        return null;
    }

    public static List<Enterprise> getAllEnterprisesProximity(Context context, String parameters) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection;
        int responseServer;

        try {
            httpURLConnection = ConnectionUtil.connect(ENTERPRISE_URL_JSON + "/proximity", "POST", true, true, token);

            OutputStream os = httpURLConnection.getOutputStream();
            os.write(parameters.getBytes());
            os.flush();
            os.close();

            responseServer = httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw new Exception("Falha na conexão com a API");
        }

        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllEnterprisesArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Enterprise> enterprises = readEnterpriseArray(jsonAllEnterprisesArray);
            return enterprises;
        }
        return null;
    }

    /**
     * Read json object enterprise
     * @param jsonPlayerObject
     * @return
     * @throws JSONException
     */
    public static Enterprise readEnterpriseObject(JSONObject jsonPlayerObject) throws JSONException {
        JSONObject jsonEnterprise = jsonPlayerObject;
        JSONObject jsonPerson = jsonEnterprise.getJSONObject("Person");

        Enterprise enterprise = new Enterprise(
                jsonPerson.getInt("id"),
                jsonPerson.getString("fullname"),
                jsonPerson.getString("username"),
                jsonPerson.getString("email"),
                "", // password not return
                jsonPerson.getString("district"),
                jsonPerson.getString("lat"),
                jsonPerson.getString("lng"),
                jsonEnterprise.getInt("id"),
                jsonEnterprise.getString("telephone")
        );
        return enterprise;
    }

    public static List<Enterprise> readEnterpriseArray(JSONArray jsonEnterpriseArray) throws JSONException {
        List<Enterprise> enterpriseList = new ArrayList<>();

        for (int i = 0; i < jsonEnterpriseArray.length(); i++){
            JSONObject jsonEnterprise = jsonEnterpriseArray.getJSONObject(i);
            JSONObject jsonPerson = jsonEnterprise.getJSONObject("Person");

            Enterprise enterprise = new Enterprise(
                    jsonPerson.getInt("id"),
                    jsonPerson.getString("fullname"),
                    jsonPerson.getString("username"),
                    jsonPerson.getString("email"),
                    "", // password not return
                    jsonPerson.getString("district"),
                    jsonPerson.getString("lat"),
                    jsonPerson.getString("lng"),
                    jsonEnterprise.getInt("id"),
                    jsonEnterprise.getString("telephone")
            );
            enterpriseList.add(enterprise);
        }

        return enterpriseList;
    }

}
