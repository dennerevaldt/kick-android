package br.unisinos.kickoffapp.asynk.gameTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Game;
import br.unisinos.kickoffapp.utils.CourtHttp;
import br.unisinos.kickoffapp.utils.GameHttp;

/**
 * Created by dennerevaldtmachado on 28/07/16.
 */
public class GetListGamesTask extends AsyncTask<Void, List<Game>, List<Game>> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;
    private Boolean showProgress;

    public GetListGamesTask(Context contextActive, Boolean showProgress) {
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
    protected List<Game> doInBackground(Void... params) {
        try {
            return GameHttp.getAllGames(context);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Game> gameListReturn) {
        super.onPostExecute(gameListReturn);
        if (showProgress)
            progressDialog.dismiss();

        if(exception != null)
            onRequestFailed(exception.getMessage());

    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
