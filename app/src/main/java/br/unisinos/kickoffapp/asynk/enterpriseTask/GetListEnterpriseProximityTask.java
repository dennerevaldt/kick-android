package br.unisinos.kickoffapp.asynk.enterpriseTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Enterprise;
import br.unisinos.kickoffapp.utils.EnterpriseHttp;

/**
 * Created by dennerevaldtmachado on 29/07/16.
 */
public class GetListEnterpriseProximityTask extends AsyncTask<String, List<Enterprise>, List<Enterprise>> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;
    private Boolean showProgress;

    // Interface for callback result
    public interface TaskResult {
        void onFinished(List<Enterprise> result);
    }

    // This is the reference to the associated listener
    private final TaskResult taskListener;

    public GetListEnterpriseProximityTask(Context contextActive, Boolean showProgress, TaskResult taskListener) {
        this.context = contextActive;
        this.showProgress = showProgress;
        this.taskListener = taskListener;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Buscando empresas próximas...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgress)
            progressDialog.show();
    }

    @Override
    protected List<Enterprise> doInBackground(String... params) {
        try {
            return EnterpriseHttp.getAllEnterprisesProximity(context, params[0]);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Enterprise> enterpriseList) {
        super.onPostExecute(enterpriseList);
        if (showProgress) {
            progressDialog.dismiss();
        }

        if(exception != null) {
            onRequestFailed(exception.getMessage());
        }

        if(this.taskListener != null) {
            this.taskListener.onFinished(enterpriseList);
        }

    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
