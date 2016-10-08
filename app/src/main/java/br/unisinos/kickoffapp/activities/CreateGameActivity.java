package br.unisinos.kickoffapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.EnterprisesListAdapter;
import br.unisinos.kickoffapp.adapters.SchedulesListSpinnerAdapter;
import br.unisinos.kickoffapp.asynk.enterpriseTask.GetListEnterpriseProximityTask;
import br.unisinos.kickoffapp.asynk.gameTask.RegisterGameTask;
import br.unisinos.kickoffapp.asynk.scheduleTask.GetAllSchedulesByIdEnterpriseTask;
import br.unisinos.kickoffapp.models.Enterprise;
import br.unisinos.kickoffapp.models.Game;
import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ConnectionUtil;

public class CreateGameActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_ERRO_PLAY_SERVICES = 1;
    private GoogleApiClient googleApiClient;
    private LatLng latLngOrigin;
    private Spinner spinnerSchedules = null;
    private TextView txtViewCourtSchedule;
    private EditText editTextEnterprise;
    private SchedulesListSpinnerAdapter dataAdapterSchedules;
    private EditText editTextName;
    private AppCompatButton btnCreate;
    private Schedule scheduleSelected;
    private List<Enterprise> enterprises;
    private Enterprise enterpriseSelected;
    private List<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.novo_jogo);

        initEditText();
        initButtonSubmit();
        initGoogleApiClient();

        editTextEnterprise = (EditText) findViewById(R.id.enterprise);
        if (editTextEnterprise != null) {
            editTextEnterprise.setInputType(InputType.TYPE_NULL);
        }
        editTextEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEnterprises(latLngOrigin);
            }
        });

        txtViewCourtSchedule = (TextView) findViewById(R.id.txtViewCourtSchedule);
        if (txtViewCourtSchedule != null) {
            txtViewCourtSchedule.setVisibility(View.GONE);
        }

        spinnerSchedules = (Spinner) findViewById(R.id.spinnerSchedules);
        if (spinnerSchedules != null) {
            spinnerSchedules.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do your own thing here
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getListSchedules(String idEnterprise) {
        scheduleList = new ArrayList<>();

        GetAllSchedulesByIdEnterpriseTask.TaskResult listener = new GetAllSchedulesByIdEnterpriseTask.TaskResult() {
            @Override
            public void onFinished(List<Schedule> result) {
                scheduleList = result;

                if (scheduleList.size() != 0){
                    dataAdapterSchedules = new SchedulesListSpinnerAdapter(CreateGameActivity.this, scheduleList);
                    spinnerSchedules.setAdapter(dataAdapterSchedules);
                    spinnerSchedules.setVisibility(View.VISIBLE);
                    txtViewCourtSchedule.setVisibility(View.VISIBLE);
                    btnCreate.setEnabled(true);

                    spinnerSchedules.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            scheduleSelected = (Schedule) dataAdapterSchedules.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinnerSchedules.setVisibility(View.GONE);
                    txtViewCourtSchedule.setVisibility(View.GONE);
                    btnCreate.setEnabled(false);

                    Toast.makeText(CreateGameActivity.this, "Não há horários disponíveis nessa empresa, escolha outra", Toast.LENGTH_LONG).show();
                }

            }
        };
        if (ConnectionUtil.hasConnection(this)) {
            GetAllSchedulesByIdEnterpriseTask task = new GetAllSchedulesByIdEnterpriseTask(this, listener);
            task.execute(idEnterprise);
        } else {
            Toast.makeText(CreateGameActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
        }
    }

    private void initEditText() {
        editTextName = (EditText) findViewById(R.id.name);
    }

    private void initButtonSubmit() {
        btnCreate = (AppCompatButton) findViewById(R.id.createButton);
        if (btnCreate != null) {
            btnCreate.setEnabled(false);
        }
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                if (name.equals("")){
                    editTextName.setError("Campo obrigatório");
                    editTextName.requestFocus();
                } else {
                    Game game = new Game();
                    game.setName(name);
                    game.setSchedule(scheduleSelected);

                    if (ConnectionUtil.hasConnection(CreateGameActivity.this)) {
                        RegisterGameTask registerGameTask = new RegisterGameTask(CreateGameActivity.this);
                        registerGameTask.execute(game);
                    }
                }

            }
        });
    }

    private void getEnterprises(LatLng latLng) {
            enterprises = new ArrayList<>();

            GetListEnterpriseProximityTask.TaskResult listner = new GetListEnterpriseProximityTask.TaskResult() {
                @Override
                public void onFinished(List<Enterprise> result) {
                    enterprises = result;

                    if (enterprises != null){
                        final AlertDialog alertDialog = new AlertDialog.Builder(CreateGameActivity.this).create();
                        LayoutInflater inflater = getLayoutInflater();
                        View convertView = inflater.inflate(R.layout.dialog_list_enterprise, null);
                        alertDialog.setView(convertView);
                        alertDialog.setTitle("Escolha uma empresa próxima de você");
                        ListView lvEnterprises = (ListView) convertView.findViewById(R.id.listViewEnterprises);
                        enterprises = enterprises == null ? new ArrayList<Enterprise>() : enterprises;
                        EnterprisesListAdapter enterprisesListAdapter = new EnterprisesListAdapter(CreateGameActivity.this, enterprises);
                        lvEnterprises.setAdapter(enterprisesListAdapter);
                        alertDialog.show();

                        lvEnterprises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                enterpriseSelected = enterprises.get(position);
                                editTextEnterprise.setText(enterpriseSelected.getFullName());
                                alertDialog.dismiss();
                                getListSchedules(String.valueOf(enterpriseSelected.getIdEnterprise()));
                            }
                        });
                    }
                }
            };

        if (ConnectionUtil.hasConnection(this)) {
            if (latLng != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("&lat=" + latLng.latitude);
                sb.append("&lng=" + latLng.longitude);

                String[] parameters = {sb.toString()};

                GetListEnterpriseProximityTask task = new GetListEnterpriseProximityTask(this, true, listner);
                task.execute(parameters);
            } else {
                getLastLocalization();
            }
        } else {
            Toast.makeText(CreateGameActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ERRO_PLAY_SERVICES && resultCode == RESULT_OK) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLastLocalization();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_ERRO_PLAY_SERVICES);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showMessageError(this, connectionResult.getErrorCode());
        }
    }

    private void getLastLocalization() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPerm();
        }  else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(CreateGameActivity.this, "Não foi possível encontrar sua localização, ative-a e tente novamente", Toast.LENGTH_LONG).show();
            }
            //latLngOrigin = new LatLng(-29.4753, -49.9849);
        }
    }

    private void showMessageError(Activity activity, final int codeError) {
        DialogFragment dialogFragment = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return GooglePlayServicesUtil.getErrorDialog(codeError, getActivity(), REQUEST_ERRO_PLAY_SERVICES);
            }
        };
        dialogFragment.show(activity.getFragmentManager(), "DIALOG_ERROR_PLAY_SERVICES");
    }

    private void requestPerm() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
}
