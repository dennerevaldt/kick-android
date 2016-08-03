package br.unisinos.kickoffapp.asynk.scheduleTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ScheduleHttp;

/**
 * Created by dennerevaldtmachado on 26/07/16.
 */
public class EditScheduleTask extends AsyncTask<Schedule, Void, Schedule> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;

    public EditScheduleTask(Context contextActive) {
        this.context = contextActive;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Editando...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Schedule doInBackground(Schedule... params) {
        try {
            ScheduleHttp scheduleHttp = new ScheduleHttp();
            return scheduleHttp.updateSchedule(params[0], context);
        } catch (Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Schedule schedule) {
        super.onPostExecute(schedule);
        progressDialog.dismiss();
        if (schedule != null) {
            onRequestSuccessOperation("Horário editado com sucesso");
        } else {
            onRequestFailed(exception.getMessage());
        }
    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private void onRequestSuccessOperation(String message) {
        Intent intent = new Intent();
        intent.putExtra("message", message);
        ((Activity)context).setResult(Activity.RESULT_OK, intent);
        ((Activity)context).finish();
    }
}