package br.unisinos.kickoffapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.utils.AuthHttp;
import br.unisinos.kickoffapp.utils.ConnectionUtil;
import br.unisinos.kickoffapp.utils.UserPreferences;

public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Exception exception = null;
    private LoginRequestHttp loginRequestHttp;
    private LoginRequestFacebookHttp loginRequestFacebookHttp;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private String nameFb;
    private String emailFb;
    private String idUserFb;

    // UI references.
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        /**
         * Denner: Verifica usuário logado
         */
        int typeUser = UserPreferences.getTypeUser(getApplicationContext());
        if (typeUser != -1) {
            if (typeUser == 0) {
                // direciona pro home empresa
                Intent intent = new Intent(LoginActivity.this, EnterpriseMainActivity.class);
                startActivity(intent);
                this.finish();
            } else if (typeUser == 1) {
                // direciona pro home jogador
                Intent intent = new Intent(LoginActivity.this, PlayerMainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
        simpleLogin();
        facebookLogin();
    }

    private void simpleLogin() {
        AppCompatButton loginSimpleButton = (AppCompatButton) findViewById(R.id.email_sign_in_button);
        if (loginSimpleButton != null) {
            loginSimpleButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder sb = new StringBuilder();
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();

                    if (username.equals("")) {
                        editTextUsername.setError("Campo obrigatório");
                        editTextUsername.requestFocus();
                    } else if (password.equals("")) {
                        editTextPassword.setError("Campo obrigatório");
                        editTextPassword.requestFocus();
                    } else {
                        sb.append("&username=" + editTextUsername.getText());
                        sb.append("&password=" + editTextPassword.getText());

                        String[] parameters = {sb.toString()};

                        if (ConnectionUtil.hasConnection(LoginActivity.this)) {
                            exception = null;
                            loginRequestHttp = new LoginRequestHttp();
                            loginRequestHttp.execute(parameters);
                        } else {
                            Toast.makeText(LoginActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void facebookLogin() {

        Button loginButton = (Button) findViewById(R.id.facebook_sign_in_button);

        if (loginButton != null) {
            loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                }
            });
        }

        mCallbackManager = new CallbackManager.Factory().create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                try {
                                    nameFb = object.getString("name");
                                    emailFb = object.getString("email");
                                    idUserFb = object.getString("id");

                                    StringBuilder sb = new StringBuilder();
                                    sb.append("&email=" + emailFb);
                                    sb.append("&password=" + idUserFb);

                                    String[] parameters = {sb.toString()};

                                    exception = null;
                                    loginRequestFacebookHttp = new LoginRequestFacebookHttp();
                                    loginRequestFacebookHttp.execute(parameters);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * Redirect page
     */
    public void goNewAccountPage(View v){
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Http login request
     */
    class LoginRequestHttp extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Autenticando...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                return AuthHttp.login(getApplicationContext(), params[0]);
            } catch (Exception e) {
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean login) {
            super.onPostExecute(login);
            progressDialog.dismiss();
            if (exception != null) {
                if (exception.getMessage() == "401") {
                    Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_LONG).show();
                } else if (exception.getMessage() == "404") {
                    Toast.makeText(LoginActivity.this, "Usuário ou senha não cadastrados", Toast.LENGTH_LONG).show();
                } else if (exception.getMessage() == "500") {
                    Toast.makeText(LoginActivity.this, "Ops, estamos com problemas... Tente mais tarde", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Falha na conexão, verifique sua rede", Toast.LENGTH_LONG).show();
                }
            } else if(login) {
                onLoginSuccess();
            }
            else {
                onLoginError();
            }
        }
    }

    /**
     * Http login facebook request
     */
    class LoginRequestFacebookHttp extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Autenticando...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                return AuthHttp.loginFacebook(getApplicationContext(), params[0]);
            } catch (Exception e) {
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean login) {
            super.onPostExecute(login);
            progressDialog.dismiss();
            if (exception != null) {
                if (exception.getMessage() == "401") {
                    Toast.makeText(LoginActivity.this, "Conta não vinculada ao facebook", Toast.LENGTH_LONG).show();
                } else if (exception.getMessage() == "404") {
                    Intent intent = new Intent(LoginActivity.this, ConfirmAccountActivity.class);
                    intent.putExtra("nameFb", nameFb);
                    intent.putExtra("emailFb", emailFb);
                    intent.putExtra("idUserFb", idUserFb);
                    startActivity(intent);
                } else if (exception.getMessage() == "500") {
                    Toast.makeText(LoginActivity.this, "Ops, estamos com problemas... Tente mais tarde", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Falha na conexão, verifique sua rede", Toast.LENGTH_LONG).show();
                }
            } else {
                onLoginSuccess();
            }
        }
    }

    /**
     * Feedbacks login
     */
    private void onLoginSuccess() {
        int typeUser = UserPreferences.getTypeUser(getApplicationContext());
        if (typeUser != -1) {
            if (typeUser == 0) {
                // direciona pro home empresa
                Intent intent = new Intent(LoginActivity.this, EnterpriseMainActivity.class);
                startActivity(intent);
                finish();
            } else if (typeUser == 1) {
                // direciona pro home jogador
                Intent intent = new Intent(LoginActivity.this, PlayerMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void onLoginError() {
        Toast.makeText(getApplicationContext(), "Usuário ou senha incorretos.", Toast.LENGTH_LONG).show();
    }
}

