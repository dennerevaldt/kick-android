package br.unisinos.kickoffapp.asynk.courtTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.utils.CourtHttp;

/**
 * Created by dennerevaldtmachado on 26/07/16.
 */
public class DeleteCourtTask extends AsyncTask<Court, Void, Boolean> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;

    public DeleteCourtTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Excluindo...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Court... params) {
        try {
            CourtHttp courtHttp = new CourtHttp();
            return courtHttp.deleteCourt(params[0], context);
        } catch (Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean deleted) {
        super.onPostExecute(deleted);
        progressDialog.dismiss();
        if (deleted) {
            onRequestSuccessOperation("Quadra excluída com sucesso");
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
