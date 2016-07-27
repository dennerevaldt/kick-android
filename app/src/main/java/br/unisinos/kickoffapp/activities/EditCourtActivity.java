package br.unisinos.kickoffapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.asynk.courtTask.DeleteCourtHttp;
import br.unisinos.kickoffapp.asynk.courtTask.EditCourtHttp;
import br.unisinos.kickoffapp.models.Court;

public class EditCourtActivity extends AppCompatActivity {
    private Court court;
    private Spinner spinnerCategories;
    private EditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_court);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        court = (Court) getIntent().getSerializableExtra("Court");

        setTitle("Editar "+court.getName());

        initCategories();
        initEditText();
        initButtonSubmit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.court_crud_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do your own thing here
                finish();
                return true;
            case R.id.action_delete:
                // remove
                AskOption();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void AskOption() {
        final AlertDialog dialogDelete = new AlertDialog.Builder(this)
            .setTitle("Excluir")
            .setMessage("Você deseja realmente excluir esta quadra?")
            .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    DeleteCourtHttp deleteCourtHttp = new DeleteCourtHttp(EditCourtActivity.this);
                    deleteCourtHttp.execute(court);
                }
            })
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .create();
        dialogDelete.show();
        dialogDelete.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dialogDelete.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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

    private void initEditText() {
        editTextName = (EditText) findViewById(R.id.name);
        editTextName.setText(court.getName());

        if (court.getCategory().equals("Futebol society (7)")){
            spinnerCategories.setSelection(0);
        } else if (court.getCategory().equals("Futebol de salão (Futsal)")) {
            spinnerCategories.setSelection(1);
        }
    }

    private void initButtonSubmit() {
        AppCompatButton btnSubmit = (AppCompatButton) findViewById(R.id.saveButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String category = spinnerCategories.getSelectedItem().toString();

                Court courtEdit = new Court(court.getIdCourt(), name, category);
                EditCourtHttp editCourtHttp = new EditCourtHttp(EditCourtActivity.this);
                editCourtHttp.execute(courtEdit);
            }
        });
    }
}
