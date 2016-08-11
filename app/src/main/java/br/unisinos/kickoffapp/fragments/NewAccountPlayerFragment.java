package br.unisinos.kickoffapp.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.PlacesAutoCompleteAdapter;
import br.unisinos.kickoffapp.models.Place;
import br.unisinos.kickoffapp.models.Player;
import br.unisinos.kickoffapp.utils.ConnectionUtil;
import br.unisinos.kickoffapp.utils.PlaceAPI;
import br.unisinos.kickoffapp.utils.PlayerHttp;



/**
 * A simple {@link Fragment} subclass.
 */
public class NewAccountPlayerFragment extends Fragment {
    private ProgressDialog progressDialog;
    private Exception exception = null;
    private RegisterPlayerHttp mRegisterPlayerTask;
    private EditText editTextFullname;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextPosition;
    private AutoCompleteTextView autocompleteView;
    private LatLng latLng;
    private TextView textViewGoLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_account_player, container, false);
        /**
         * Values text
         */
        editTextFullname = (EditText) view.findViewById(R.id.fullname);
        editTextUsername = (EditText) view.findViewById(R.id.username);
        editTextPassword = (EditText) view.findViewById(R.id.password);
        editTextEmail = (EditText) view.findViewById(R.id.email);
        editTextPosition = (EditText) view.findViewById(R.id.position);
        textViewGoLogin = (TextView) view.findViewById(R.id.textViewGoLogin);

        textViewGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        autocompleteView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity()));
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pega item da lista selecionado
                Place place = (Place) parent.getItemAtPosition(position);
                // Set com a descrição do lugar o auto complete
                autocompleteView.setText(place.getDescription());
                autocompleteView.clearFocus();
                // Busca lat e lng pelo id do local escolhido
                PlaceAPI placeApi = new PlaceAPI();
                latLng = placeApi.getLatLongLocale(place.getIdPlace());
            }
        });

        Button btnRegisterPLayer = (Button) view.findViewById(R.id.register_player_button);
        btnRegisterPLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtil.hasConnection(getContext())) {
                    if (checkInputs()) {
                        if (latLng != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("fullname=" + editTextFullname.getText());
                            sb.append("&username=" + editTextUsername.getText());
                            sb.append("&password=" + editTextPassword.getText());
                            sb.append("&email=" + editTextEmail.getText());
                            sb.append("&district=" + autocompleteView.getText() );
                            sb.append("&lat=" + latLng.latitude);
                            sb.append("&lng=" + latLng.longitude);
                            sb.append("&position=" + editTextPosition.getText());

                            String[] parameters = {sb.toString()};

                            mRegisterPlayerTask = new RegisterPlayerHttp();
                            mRegisterPlayerTask.execute(parameters);
                        } else {
                            autocompleteView.setError("Localidade inválida, pesquise e selecione uma válida");
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Sem conexão", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkLat();

        return view;
    }

    private void clearFields () {
        editTextFullname.setText("");
        editTextUsername.setText("");
        editTextPassword.setText("");
        editTextEmail.setText("");
        autocompleteView.setText("");
        editTextPosition.setText("");

        editTextFullname.clearFocus();
        editTextUsername.clearFocus();
        editTextPassword.clearFocus();
        editTextEmail.clearFocus();
        autocompleteView.clearFocus();
        editTextPosition.clearFocus();
    }

    private void checkLat() {
        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                latLng = null;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Boolean checkInputs() {
        String fullname = editTextFullname.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String position = editTextPosition.getText().toString();

        if (fullname.equals("")){
            editTextFullname.setError("Campo obrigatório");
            return false;
        }

        if (username.equals("")){
            editTextUsername.setError("Campo obrigatório");
            return false;
        }

        if (password.equals("")){
            editTextPassword.setError("Campo obrigatório");
            return false;
        }

        if (email.equals("")){
            editTextEmail.setError("Campo obrigatório");
            return false;
        }

        if (position.equals("")){
            editTextPosition.setError("Campo obrigatório");
            return false;
        }

        return true;
    }

    class RegisterPlayerHttp extends AsyncTask<String, Void, Player> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
            progressDialog.setMessage("Cadastrando...");
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
            if (exception != null && player == null) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                clearFields();
                Toast.makeText(getContext(), "Jogador "+player.getFullName()+" criado com sucesso.", Toast.LENGTH_LONG).show();
            }
        }
    }

}


