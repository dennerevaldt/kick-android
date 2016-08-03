package br.unisinos.kickoffapp.asynk.courtTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.utils.CourtHttp;

/**
 * Created by dennerevaldtmachado on 26/07/16.
 */
public class GetListCourtsTask extends AsyncTask<Void, List<Court>, List<Court>> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;
    private Boolean showProgress;

    public GetListCourtsTask(Context contextActive, Boolean showProgress) {
        this.context = contextActive;
        this.showProgress = showProgress;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Carregando informações...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showProgress)
            progressDialog.show();
    }

    @Override
    protected List<Court> doInBackground(Void... params) {
        try {
            return CourtHttp.getAllCourts(context);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Court> courtListReturn) {
        super.onPostExecute(courtListReturn);
        if (showProgress)
            progressDialog.dismiss();

        if(exception != null)
            onRequestFailed(exception.getMessage());

    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
