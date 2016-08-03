package br.unisinos.kickoffapp.asynk.scheduleTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ScheduleHttp;

/**
 * Created by dennerevaldtmachado on 26/07/16.
 */
public class GetAllSchedulesByIdEnterpriseTask extends AsyncTask<String, List<Schedule>, List<Schedule>> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;

    // Interface for callback result
    public interface TaskResult {
        void onFinished(List<Schedule> result);
    }

    // This is the reference to the associated listener
    private final TaskResult taskListener;

    public GetAllSchedulesByIdEnterpriseTask(Context contextActive, TaskResult taskListener) {
        this.context = contextActive;
        this.taskListener = taskListener;
        progressDialog = new ProgressDialog(contextActive, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Buscando horários...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected List<Schedule> doInBackground(String... params) {
        try{
            return ScheduleHttp.getAllByIdEnterprise(context, params[0]);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Schedule> scheduleList) {
        super.onPostExecute(scheduleList);
        progressDialog.dismiss();
        if(exception != null) {
            onRequestFailed(exception.getMessage());
        }

        if(this.taskListener != null) {
            this.taskListener.onFinished(scheduleList);
        }
    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
