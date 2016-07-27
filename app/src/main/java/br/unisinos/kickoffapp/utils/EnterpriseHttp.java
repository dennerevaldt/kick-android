package br.unisinos.kickoffapp.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import br.unisinos.kickoffapp.models.Enterprise;

/**
 * Created by dennerevaldtmachado on 29/06/16.
 */
public class EnterpriseHttp {
    public static final String PLAYER_URL_JSON = "/enterprise";

    /**
     * Create new enterprise
     * @param parameters
     * @return
     * @throws Exception
     */
    public Enterprise createEnterprise (String parameters) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(PLAYER_URL_JSON, "POST", true, true, null);

        //Enterprise enterpriseLogin = UserPreferences.getUserEnteprise(context);
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

}
