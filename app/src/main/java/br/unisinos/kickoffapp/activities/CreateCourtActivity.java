package br.unisinos.kickoffapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.asynk.courtTask.RegisterCourtHttp;
import br.unisinos.kickoffapp.models.Court;

public class CreateCourtActivity extends AppCompatActivity {
    private Spinner spinnerCategories;
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_court);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.nova_quadra);

        editTextName = (EditText) findViewById(R.id.name);

        initCategories();
        initButtonCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do your own thing here
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void initCategories(){
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategory);

        final List<String> listSpinnerTypes = new ArrayList<>();
        listSpinnerTypes.add("Futebol society (7)");
        listSpinnerTypes.add("Futebol de salão (Futsal)");

        ArrayAdapter<String> dataAdapterTypes = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listSpinnerTypes);
        dataAdapterTypes.setDropDownViewResource(R.layout.spinner_item_court_category);
        spinnerCategories.setAdapter(dataAdapterTypes);
    }

    private void initButtonCreate() {
        AppCompatButton btnCreate = (AppCompatButton) findViewById(R.id.createButton);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String category = spinnerCategories.getSelectedItem().toString();

                Court court = new Court("", name, category);
                RegisterCourtHttp registerCourtHttp = new RegisterCourtHttp(CreateCourtActivity.this);
                registerCourtHttp.execute(court);
            }
        });
    }
}
