package br.unisinos.kickoffapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.unisinos.kickoffapp.R;
import br.unisinos.kickoffapp.adapters.CourtsListAdapter;
import br.unisinos.kickoffapp.adapters.CourtsListSpinnerAdapter;
import br.unisinos.kickoffapp.asynk.courtTask.GetListCourtsTask;
import br.unisinos.kickoffapp.asynk.scheduleTask.RegisterScheduleTask;
import br.unisinos.kickoffapp.models.Court;
import br.unisinos.kickoffapp.models.Schedule;
import br.unisinos.kickoffapp.utils.ConnectionUtil;

public class CreateScheduleActivity extends AppCompatActivity {
    private int day;
    private int month;
    private int year;
    private EditText editTextDate;
    private EditText editTextHorary;
    private Spinner spinnerCategories;
    private CourtsListSpinnerAdapter dataAdapterTypes;
    private Court courtSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Novo horário");

        initEditTextDatePicker();
        initEditTextTimePicker();
        initListCourts();
        initButtonSubmit();
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

    public void initEditTextDatePicker() {
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        editTextDate.setText(String.format("%02d/%02d/%02d", day, month, year));

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

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void initEditTextTimePicker() {
        editTextHorary = (EditText) findViewById(R.id.editTextHorary);

        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        editTextHorary.setText(String.format("%02d:%02d", hour, minute));

        editTextHorary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialog();
            }
        });
        editTextHorary.setInputType(InputType.TYPE_NULL);
    }

    public void TimeDialog() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                editTextHorary.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }
        };

        TimePickerDialog tmDialog = new TimePickerDialog(this, listener, hour, minute, true);
        tmDialog.show();
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
        GetListCourtsTask getListCourtsTask = new GetListCourtsTask(CreateScheduleActivity.this, true);
        List<Court> courtListReturn = null;
        try {
            courtListReturn = getListCourtsTask.execute().get();
        } catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }

        dataAdapterTypes = new CourtsListSpinnerAdapter(this, courtListReturn);
        spinnerCategories.setAdapter(dataAdapterTypes);
    }

    private void initButtonSubmit() {
        AppCompatButton createButton = (AppCompatButton) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = editTextDate.getText().toString();
                String horary = editTextHorary.getText().toString();

                Schedule schedule = new Schedule(
                        "",
                        horary,
                        date,
                        courtSelected
                );

                if (ConnectionUtil.hasConnection(CreateScheduleActivity.this)) {
                    RegisterScheduleTask registerScheduleTask = new RegisterScheduleTask(CreateScheduleActivity.this);
                    registerScheduleTask.execute(schedule);
                } else {
                    Toast.makeText(CreateScheduleActivity.this, "Sem conexão", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
