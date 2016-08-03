package br.unisinos.kickoffapp.asynk.gameTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Game;
import br.unisinos.kickoffapp.utils.CourtHttp;
import br.unisinos.kickoffapp.utils.GameHttp;

/**
 * Created by dennerevaldtmachado on 28/07/16.
 */
public class RegisterGameTask extends AsyncTask<Game, Void, Game> {
    private ProgressDialog progressDialog;
    private Context context;
    private Exception exception;

    public RegisterGameTask(Context contextActive) {
        this.context = contextActive;
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Criando novo jogo...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Game doInBackground(Game... params) {
        try {
            GameHttp gameHttp = new GameHttp();
            return gameHttp.createGame(params[0], context);
        } catch (Exception e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Game game) {
        super.onPostExecute(game);
        progressDialog.dismiss();
        if (game != null) {
            onRequestSuccessOperation("Jogo registrado com sucesso");
        } else {
            onRequestFailed(exception.getMessage());
        }
    }

    private void onRequestSuccessOperation(String message) {
        Intent intent = new Intent();
        intent.putExtra("message", message);
        ((Activity)context).setResult(Activity.RESULT_OK, intent);
        ((Activity)context).finish();
    }

    private void onRequestFailed(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
