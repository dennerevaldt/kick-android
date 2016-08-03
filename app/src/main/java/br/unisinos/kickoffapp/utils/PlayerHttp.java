package br.unisinos.kickoffapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.models.Player;

/**
 * Created by dennerevaldtmachado on 28/06/16.
 */
public class PlayerHttp {
    private static final String PLAYER_URL_JSON = "/player";

    /**
     * Create new player
     * @param parameters
     * @return Player created
     * @throws Exception
     */
    public Player createPlayer (String parameters) throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(PLAYER_URL_JSON, "POST", true, true, null);

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(parameters.getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonPlayerObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            Player player = readPlayerObject(jsonPlayerObject);
            return player;
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Usuário já existe");
        }
        return null;
    }

    /**
     * Get all players registers
     * @return List Player
     */
    public static List<Player> getAllPlayers() throws Exception {
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(PLAYER_URL_JSON, "GET", true, false, null);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllPlayersArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Player> players = readPlayersArray(jsonAllPlayersArray);
            return players;
        }
        return null;
    }

    /**
     * Read json array players
     * @param jsonPlayersArray
     * @return List Player
     * @throws JSONException
     */
    public static List<Player> readPlayersArray (JSONArray jsonPlayersArray) throws JSONException {
        List<Player> playerList = new ArrayList<>();

        for (int i = 0; i < jsonPlayersArray.length(); i++){
            JSONObject jsonPlayer = jsonPlayersArray.getJSONObject(i);
            JSONObject jsonPerson = jsonPlayer.getJSONObject("Person");
            Player player = new Player(
                    jsonPerson.getInt("id"),
                    jsonPerson.getString("fullname"),
                    jsonPerson.getString("username"),
                    jsonPerson.getString("email"),
                    "", // password not return
                    jsonPerson.getString("district"),
                    jsonPerson.getString("lat"),
                    jsonPerson.getString("lng"),
                    jsonPlayer.getInt("id"),
                    jsonPlayer.getString("position")
            );
            playerList.add(player);
        }

        return playerList;
    }

    /**
     * Read json object player
     * @param jsonPlayerObject
     * @return Player
     * @throws JSONException
     */
    public static Player readPlayerObject(JSONObject jsonPlayerObject) throws JSONException {
        JSONObject jsonPlayer = jsonPlayerObject;
        JSONObject jsonPerson = jsonPlayer.getJSONObject("Person");

        Player player = new Player(
                jsonPerson.getInt("id"),
                jsonPerson.getString("fullname"),
                jsonPerson.getString("username"),
                jsonPerson.getString("email"),
                "", // password not return
                jsonPerson.getString("district"),
                jsonPerson.getString("lat"),
                jsonPerson.getString("lng"),
                jsonPlayer.getInt("id"),
                jsonPlayer.getString("position")
        );
        return player;
    }
}
