package br.unisinos.kickoffapp.asynk.scheduleTask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ScheduleHttp;

/**
 * Created by dennerevaldtmachado on 26/07/16.
 */
public class GetScheduleListHttp extends AsyncTask<String, List<Schedule>, List<Schedule>> {
    private Context context;
    private Exception exception;

    public GetScheduleListHttp(Context contextActive) {
        this.context = contextActive;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Schedule> doInBackground(String... params) {
        try{
            return ScheduleHttp.getAllSchedules(context);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Schedule> scheduleList) {
        super.onPostExecute(scheduleList);
        if(exception != null) {
            onRequestFailed(exception.getMessage());
        }
    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
