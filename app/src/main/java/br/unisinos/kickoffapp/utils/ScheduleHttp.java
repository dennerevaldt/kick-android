package br.unisinos.kickoffapp.utils;

import android.content.Context;
import android.util.Log;

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
import br.unisinos.kickoffapp.models.Schedule;

/**
 * Created by dennerevaldtmachado on 20/07/16.
 */
public class ScheduleHttp {
    public static final String SCHEDULE_URL_JSON = "/schedule";

    public Schedule createSchedule (Schedule schedule, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(SCHEDULE_URL_JSON, "POST", true, true, token);

        StringBuilder sb = new StringBuilder();
        sb.append("&horary=" + schedule.getHorary());
        sb.append("&date=" + schedule.getDateFormatMySql());
        sb.append("&court_id=" + schedule.getCourt().getIdCourt());

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(sb.toString().getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonScheduleObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            return readScheduleObject(jsonScheduleObject);
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao inserir horário.");
        }
        return null;
    }

    public Boolean deleteSchedule (Schedule schedule, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        String ID_SCHEDULE = "/" + schedule.getIdSchedule();
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(SCHEDULE_URL_JSON+ID_SCHEDULE, "DELETE", true, true, token);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            return true;
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao excluir horário.");
        }
        return null;
    }

    public Schedule updateSchedule (Schedule schedule, Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        String ID_SCHEDULE = "/" + schedule.getIdSchedule();
        Enterprise enterpriseLogin = UserPreferences.getUserEnteprise(context);

        HttpURLConnection httpURLConnection = ConnectionUtil.connect(SCHEDULE_URL_JSON+ID_SCHEDULE, "PUT", true, true, token);

        StringBuilder sb = new StringBuilder();
        sb.append("&horary=" + schedule.getHorary());
        sb.append("&date=" + schedule.getDateFormatMySql());
        sb.append("&court_id=" + schedule.getCourt().getIdCourt());
        sb.append("&enterprise_id=" + enterpriseLogin.getIdEnterprise());

        OutputStream os = httpURLConnection.getOutputStream();
        os.write(sb.toString().getBytes());
        os.flush();
        os.close();

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONObject jsonScheduleObject = new JSONObject(ConnectionUtil.bytesForString(inputStream));
            return readScheduleObject(jsonScheduleObject);
        } else if (responseServer == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            throw new Exception("Problemas ao editar horário.");
        }
        return null;
    }

    /**
     * Get all players registers
     * @return List Player
     */
    public static List<Schedule> getAllSchedules(Context context) throws Exception {
        String token = UserPreferences.getToken(context);
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(SCHEDULE_URL_JSON, "GET", true, false, token);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllSchedulesArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Schedule> schedules = readScheduleArray(jsonAllSchedulesArray);
            return schedules;
        }
        return new ArrayList<>();
    }

    public static List<Schedule> getAllByIdEnterprise(Context context, String idEnterprise) throws Exception {
        String token = UserPreferences.getToken(context);
        String urlID = "/enterprise/"+idEnterprise;
        HttpURLConnection httpURLConnection = ConnectionUtil.connect(SCHEDULE_URL_JSON + urlID, "GET", true, false, token);

        int responseServer = httpURLConnection.getResponseCode();
        if (responseServer == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            JSONArray jsonAllSchedulesArray = new JSONArray(ConnectionUtil.bytesForString(inputStream));
            List<Schedule> schedules = readScheduleArray(jsonAllSchedulesArray);
            return schedules;
        }
        return null;
    }

    public static Schedule readScheduleObject(JSONObject jsonSchedule) throws JSONException {

            Schedule schedule = new Schedule(
                    jsonSchedule.getString("id"),
                    jsonSchedule.getString("horary"),
                    jsonSchedule.getString("date"),
                    new Court(
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("id") : "",
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("name") : "",
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("category") : ""
                    )
            );

        return schedule;
    }

    public static List<Schedule> readScheduleArray(JSONArray jsonScheduleArray) throws JSONException {
        List<Schedule> scheduleList = new ArrayList<>();

        for (int i = 0; i < jsonScheduleArray.length(); i++){
            JSONObject jsonSchedule = jsonScheduleArray.getJSONObject(i);

            Schedule schedule = new Schedule(
                    jsonSchedule.getString("id"),
                    jsonSchedule.getString("horary"),
                    jsonSchedule.getString("date"),
                    new Court(
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("id") : "",
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("name") : "",
                            jsonSchedule.has("Court") && !jsonSchedule.isNull("Court") ? jsonSchedule.getJSONObject("Court").getString("category") : ""
                    )
            );
            scheduleList.add(schedule);
        }

        return scheduleList;
    }
}
