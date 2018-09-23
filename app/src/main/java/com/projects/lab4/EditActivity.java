package com.projects.lab4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    EditText ename;
    Button done;
    Button cancel;
    TextView eventDate;
    TextView eventTime;
    TextView eventtv;
    TextView datetv;
    TextView timetv;

    DBManager dbManager;
    Calendar myCalendar;

    private void openDB(){
        dbManager = new DBManager(this);
        dbManager.open();

    }
    private void closeDB(){
        dbManager.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        openDB();

        eventtv = (TextView) findViewById(R.id.event);
        datetv = (TextView) findViewById(R.id.date);
        timetv = (TextView) findViewById(R.id.time);

        dbManager = new DBManager(this);

        ename = (EditText) findViewById(R.id.eName);
        eventDate = (TextView) findViewById(R.id.textViewDate);
        eventTime = (TextView) findViewById(R.id.textViewTime);
        myCalendar = Calendar.getInstance();


        String speechtext = getIntent().getStringExtra("Event");
        String speechDate = getIntent().getStringExtra("Day")+", "+getIntent().getStringExtra("Month")+" "
                +getIntent().getStringExtra("DayNo")+", "+getIntent().getStringExtra("Year");
        String speechTime = getIntent().getStringExtra("Time");

        ename.setText(speechtext);
        eventDate.setText(speechDate);
        eventTime.setText(speechTime);

        //String speechtext = getIntent().getStringExtra("input eventName");
       // String speechDate = getIntent().getStringExtra("input dateDay" + "input dateMonth" + "input datedayn" + "input dateyear");
       // String dateEvent = getIntent().getStringExtra("");
       // String speechTime = getIntent().getStringExtra("input time");



        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        done = (Button) findViewById(R.id.done);
        cancel = (Button) findViewById(R.id.cancel);


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = ename.getText().toString();
                String d = eventDate.getText().toString();
                String t = eventTime.getText().toString();
                dbManager.insertRow(n, d, t);

                Intent done = new Intent(EditActivity.this, EventsActivity.class);
                startActivity(done);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(EditActivity.this, EventsActivity.class);
                startActivity(cancel);
            }
        });


    }
    private void updateLabel(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        eventDate.setText(sdf.format(myCalendar.getTime()));
    }


}
