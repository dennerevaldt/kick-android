package br.unisinos.kickoffapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.CourtsListSpinnerAdapter;
import br.unisinos.kickoffapp.asynk.courtTask.GetListCourtsTask;
import br.unisinos.kickoffapp.asynk.scheduleTask.DeleteScheduleTask;
import br.unisinos.kickoffapp.asynk.scheduleTask.EditScheduleTask;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ConnectionUtil;

public class EditScheduleActivity extends AppCompatActivity {
    private Schedule schedule;
    private Spinner spinnerCategories;
    private CourtsListSpinnerAdapter dataAdapterTypes;
    private Court courtSelected;
    private EditText editTextDate;
    private EditText editTextHorary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schedule = (Schedule) getIntent().getSerializableExtra("Schedule");

        setTitle("Editar horário");

        initListCourts();
        initEditTextDatePicker();
        initEditTextTimePicker();
        initButtonSubmit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.schedule_crud_menu, menu);
        return true;
    }

    private void AskOption() {
        final AlertDialog dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Excluir")
                .setMessage("Você deseja realmente excluir este horário?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DeleteScheduleTask deleteScheduleTask = new DeleteScheduleTask(EditScheduleActivity.this);
                        deleteScheduleTask.execute(schedule);
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

    public void initListCourts() {
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courtSelected = (Court) dataAdapterTypes.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCategoriesList();
    }

    private void getCategoriesList() {
        GetListCourtsTask getListCourtsTask = new GetListCourtsTask(EditScheduleActivity.this, true);
        List<Court> courtListReturn = null;
        try {
            courtListReturn = getListCourtsTask.execute().get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        dataAdapterTypes = new CourtsListSpinnerAdapter(this, courtListReturn);
        spinnerCategories.setAdapter(dataAdapterTypes);

        for (int i = 0;  i < courtListReturn.size(); i++) {
            if (courtListReturn.get(i).getIdCourt().equals(schedule.getCourt().getIdCourt())) {
                spinnerCategories.setSelection(i);
            }
        }
    }

    public void initEditTextDatePicker() {
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate.setText(schedule.getDateFormat());

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
        editTextDate.setInputType(InputType.TYPE_NULL);
    }

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                editTextDate.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear, year));
            }
        };

        int day = schedule.getDay();
        int month = schedule.getMonth();
        int year = schedule.getYear();

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void initEditTextTimePicker() {
        editTextHorary = (EditText) findViewById(R.id.editTextHorary);
        editTextHorary.setText(schedule.getHoraryFormat());

        editTextHorary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog();
            }
        });
        editTextHorary.setInputType(InputType.TYPE_NULL);
    }

    public void TimeDialog() {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                editTextHorary.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog tmDialog = new TimePickerDialog(this, listener, schedule.getHour(), schedule.getMinutes(), true);
        tmDialog.show();
    }

    private void initButtonSubmit() {
        AppCompatButton btnSubmit = (AppCompatButton) findViewById(R.id.saveButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = editTextHorary.getText().toString();
                String date = editTextDate.getText().toString();

                Schedule scheduleEdit = new Schedule(schedule.getIdSchedule(), hour, date, courtSelected);

                if (ConnectionUtil.hasConnection(EditScheduleActivity.this)) {
                    EditScheduleTask editScheduleTask = new EditScheduleTask(EditScheduleActivity.this);
                    editScheduleTask.execute(scheduleEdit);
                } else {
                    Toast.makeText(EditScheduleActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
