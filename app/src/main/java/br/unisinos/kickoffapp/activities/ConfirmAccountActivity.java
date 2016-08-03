package br.unisinos.kickoffapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.PlacesAutoCompleteAdapter;
import br.unisinos.kickoffapp.models.Enterprise;
import br.unisinos.kickoffapp.models.Place;
import br.unisinos.kickoffapp.models.Player;
import br.unisinos.kickoffapp.utils.AuthHttp;
import br.unisinos.kickoffapp.utils.ConnectionUtil;
import br.unisinos.kickoffapp.utils.EnterpriseHttp;
import br.unisinos.kickoffapp.utils.PlaceAPI;
import br.unisinos.kickoffapp.utils.PlayerHttp;
import br.unisinos.kickoffapp.utils.UserPreferences;

public class ConfirmAccountActivity extends AppCompatActivity {
    private EditText editTextPosition;
    private EditText editTextTelephone;
    private EditText editTextUsername;
    private TextInputLayout inputLayoutPosition;
    private TextInputLayout inputLayoutTelephone;
    private Spinner spinnerTypes;
    private AutoCompleteTextView autocompleteView;
    private String nameFb;
    private String emailFb;
    private String idUserFb;
    private LatLng latLng;
    private ProgressDialog progressDialog;
    private Exception exception = null;
    private RegisterPlayerHttp mRegisterPlayerTask;
    private RegisterEnterpriseHttp mRegisterEnterpriseTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);
        setTitle("Confirmar conta");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        nameFb = extras.getString("nameFb");
        emailFb = extras.getString("emailFb");
        idUserFb = extras.getString("idUserFb");

        editTextPosition = (EditText) findViewById(R.id.position);

        editTextTelephone = (EditText) findViewById(R.id.telephone);
        editTextTelephone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));

        editTextUsername = (EditText) findViewById(R.id.username);

        inputLayoutPosition = (TextInputLayout) findViewById(R.id.positionInput);
        inputLayoutTelephone = (TextInputLayout) findViewById(R.id.telephoneInput);

        TextView textViewMessage = (TextView) findViewById(R.id.message);
        if (textViewMessage != null) {
            textViewMessage.setText("Olá "+nameFb+", seu cadastro está quase concluído! Informe somente mais alguns campos para finalizar.");
        }

        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this));
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pega item da lista selecionado
                Place place = (Place) parent.getItemAtPosition(position);
                // Set com a descrição do lugar o auto complete
                autocompleteView.setText(place.getDescription());
                // Busca lat e lng pelo id do local escolhido
                PlaceAPI placeApi = new PlaceAPI();
                latLng = placeApi.getLatLongLocale(place.getIdPlace());
            }
        });

        Button btnConfirmAccount = (Button) findViewById(R.id.confirm_account_button);
        if (btnConfirmAccount != null) {
            btnConfirmAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ConnectionUtil.hasConnection(ConfirmAccountActivity.this)) {
                        StringBuilder sb = new StringBuilder();
                        String[] parameters;
                        String username = editTextUsername.getText().toString();
                        String district = autocompleteView.getText().toString();

                        if (username.equals("")){
                            editTextUsername.setError("Campo obrigatório");
                            return;
                        }

                        sb.append("fullname=" + nameFb);
                        sb.append("&username=" + username);
                        sb.append("&password=" + idUserFb);
                        sb.append("&email=" + emailFb);

                        if (spinnerTypes.getSelectedItem().toString().equals("Jogador")) {

                            String position = editTextPosition.getText().toString();

                            if (position.equals("")) {
                                editTextPosition.setError("Campo obrigatório");
                                return;
                            } else if (district.equals("")){
                                autocompleteView.setError("Campo obrigatório");
                                return;
                            }

                            sb.append("&position=" + position);
                            sb.append("&district=" + district);
                            sb.append("&lat=" + latLng.latitude);
                            sb.append("&lng=" + latLng.longitude);
                            parameters = new String[]{sb.toString()};

                            mRegisterPlayerTask = new RegisterPlayerHttp();
                            mRegisterPlayerTask.execute(parameters);

                        } else if (spinnerTypes.getSelectedItem().toString().equals("Empresa")) {

                            String telephone = editTextTelephone.getText().toString();

                            if (telephone.equals("")) {
                                editTextTelephone.setError("Campo obrigatório");
                                return;
                            } else if (district.equals("")){
                                autocompleteView.setError("Campo obrigatório");
                                return;
                            }

                            sb.append("&telephone=" + telephone);
                            sb.append("&district=" + district);
                            sb.append("&lat=" + latLng.latitude);
                            sb.append("&lng=" + latLng.longitude);
                            parameters = new String[]{sb.toString()};

                            mRegisterEnterpriseTask = new RegisterEnterpriseHttp();
                            mRegisterEnterpriseTask.execute(parameters);

                        }
                    } else {
                        Toast.makeText(ConfirmAccountActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        initTypes();
    }

    private void initTypes(){
        spinnerTypes = (Spinner) findViewById(R.id.spinnerType);

        final List<String> listSpinnerTypes = new ArrayList<String>();
        listSpinnerTypes.add("Empresa");
        listSpinnerTypes.add("Jogador");

        ArrayAdapter<String> dataAdapterTypes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listSpinnerTypes);
        dataAdapterTypes.setDropDownViewResource(R.layout.spinner_item_type_account);
        spinnerTypes.setAdapter(dataAdapterTypes);

        spinnerTypes.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getSelectedItem().toString();
                TextView textViewSelectedItem = (TextView) parent.getChildAt(0);
                if (textViewSelectedItem != null) {
                    textViewSelectedItem.setTextColor(Color.WHITE);
                }

                if (selectedItem.equals("Empresa")) {
                    inputLayoutTelephone.setVisibility(View.VISIBLE);
                    inputLayoutPosition.setVisibility(View.GONE);
                } else if (selectedItem.equals("Jogador")){
                    inputLayoutPosition.setVisibility(View.VISIBLE);
                    inputLayoutTelephone.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class RegisterPlayerHttp extends AsyncTask<String, Void, Player> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfirmAccountActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Confirmando...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Player doInBackground(String... params) {
            PlayerHttp playerHttp = new PlayerHttp();
            try {
                Player playerCreated = playerHttp.createPlayer(params[0]);
                return playerCreated;
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Player player) {
            super.onPostExecute(player);
            progressDialog.dismiss();
            if (exception != null) {
                Toast.makeText(ConfirmAccountActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                loginAfterRegister(emailFb, idUserFb);
            }
        }
    }

    class RegisterEnterpriseHttp extends AsyncTask<String, Void, Enterprise> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfirmAccountActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Confirmando...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Enterprise doInBackground(String... params) {
            EnterpriseHttp enterpriseHttp = new EnterpriseHttp();
            try {
                Enterprise enterpriseCreated = enterpriseHttp.createEnterprise(params[0]);
                return enterpriseCreated;
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Enterprise enterprise) {
            super.onPostExecute(enterprise);
            progressDialog.dismiss();
            if (exception != null && enterprise == null) {
                Toast.makeText(ConfirmAccountActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                loginAfterRegister(emailFb, idUserFb);
            }
        }
    }

    private void loginAfterRegister(String email, String pass) {
        StringBuilder sb = new StringBuilder();
        sb.append("&email=" + email);
        sb.append("&password=" + pass);

        String[] parameters = {sb.toString()};

        LoginRequestFacebookHttp loginRequestFacebookHttp = new LoginRequestFacebookHttp();
        loginRequestFacebookHttp.execute(parameters);
    }

    /**
     * Http login facebook request
     */
    class LoginRequestFacebookHttp extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ConfirmAccountActivity.this, R.style.AppTheme_Dark_Dialog);
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
                Toast.makeText(ConfirmAccountActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                onLoginSuccess();
            }
        }
    }

    private void onLoginSuccess() {
        int typeUser = UserPreferences.getTypeUser(getApplicationContext());
        if (typeUser != -1) {
            if (typeUser == 0) {
                // direciona pro home empresa
                Intent intent = new Intent(ConfirmAccountActivity.this, EnterpriseMainActivity.class);
                startActivity(intent);
                finish();
            } else if (typeUser == 1) {
                // direciona pro home jogador
                Intent intent = new Intent(ConfirmAccountActivity.this, PlayerMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
