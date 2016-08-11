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
import br.unisinos.kickoffapp.models.Game;

/**
 * Created by dennerevaldtmachado on 28/07/16.
 */
public class GameHttp {
    private static final String GAME_URL_JSON = "/game";

    /**
     * create new game
     * @param game
     * @param context
     * @return Game
     * @throws Exception
     */
    public Game createGame(Game game, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(GAME_URL_JSON, "POST", true, true, token);

        StringBuilder sb = new StringBuilder();
        sb.append("name=" + game.getName());
        sb.append("&schedule_id=" + game.getSchedule().getIdSchedule());

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(sb.toString().getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            //InputStream inputStream = httpURLConnection.getInputStream();
            //JSONObject jsonGameObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            //Game gameReturn = readGameObject(jsonGameObject);
            //return gameReturn;
            return new Game();
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Ops, problemas ao criar jogo");
        }
        return null;
    }

    public Boolean deleteGame(){
        return null;
    }

    public Game updateGame(){
        return null;
    }

    /**
     * get all games
     * @param context
     * @return List Game
     * @throws Exception
     */
    public static List<Game> getAllGames(Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection;
        int responseServer;

        try {
            httpURLConnection = ConnectionUtil.connect(GAME_URL_JSON, "GET", true, false, token);
            responseServer = httpURLConnection.getResponseCode();
        } catch (Exception e){
            throw new Exception("Falha na conexão com a API");
        }

        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllGamesArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Game> games = readGamesArray(jsonAllGamesArray);
            return games;
        }
        return null;
    }

    public Game getOneGame() {
        return null;
    }

    /**
     *
     * @param jsonObject
     * @return Game
     * @throws JSONException
     */
    public static Game readGameObject(JSONObject jsonObject) throws JSONException {
        Game game = new Game(
            jsonObject.has("id") ? jsonObject.getString("id") : "",
            jsonObject.has("name") ? jsonObject.getString("name"): "",
            jsonObject.has("creator_id") ? jsonObject.getString("creator_id") : "",
            jsonObject.has("Schedule") && !jsonObject.isNull("Schedule") ? ScheduleHttp.readScheduleObject(jsonObject.getJSONObject("Schedule")) : null,
            jsonObject.has("Players") && !jsonObject.isNull("Players") ? PlayerHttp.readPlayersArray(jsonObject.getJSONArray("Players")) : null,
            jsonObject.getJSONObject("Schedule").has("Court") && !jsonObject.getJSONObject("Schedule").isNull("Court") ? CourtHttp.readCourtObject(jsonObject.getJSONObject("Schedule").getJSONObject("Court")) : null
        );
        return game;
    }

    /**
     *
     * @param jsonGamesArray
     * @return List Game
     * @throws JSONException
     */
    public static List<Game> readGamesArray (JSONArray jsonGamesArray) throws JSONException {
        List<Game> gamesList = new ArrayList<>();

        for (int i = 0; i < jsonGamesArray.length(); i++){
            JSONObject jsonGame = jsonGamesArray.getJSONObject(i);

            Game game = new Game(
                jsonGame.has("id") ? jsonGame.getString("id") : "",
                jsonGame.has("name") ? jsonGame.getString("name"): "",
                jsonGame.has("creator_id") ? jsonGame.getString("creator_id") : "",
                jsonGame.has("Schedule") && !jsonGame.isNull("Schedule") ? ScheduleHttp.readScheduleObject(jsonGame.getJSONObject("Schedule")) : null,
                jsonGame.has("Players") && !jsonGame.isNull("Players") ? PlayerHttp.readPlayersArray(jsonGame.getJSONArray("Players")) : null,
                jsonGame.getJSONObject("Schedule").has("Court") && !jsonGame.getJSONObject("Schedule").isNull("Court") ? CourtHttp.readCourtObject(jsonGame.getJSONObject("Schedule").getJSONObject("Court")) : null
            );
            gamesList.add(game);
        }
        return gamesList;
    }
}
