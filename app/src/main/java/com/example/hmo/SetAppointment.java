package com.example.hmo;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetAppointment extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private Button submit, goHome;
    private Spinner spinnerChoose;
    private CalendarView calendar;
    private NewDoctor doctor;
    private DatabaseReference refdb;
    private String date, dbdate, first_apt, second_apt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        setValues();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "." + (month + 1) + "." + year;
                dbdate = date.replace(".", "");
            }
        });
        submit.setOnClickListener(v -> ConfirmWorkday());
        goHome.setOnClickListener(v -> this.finish());
    }

    private void setValues() {
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        TextView doctorName = findViewById(R.id.txt_DSchedulesName);
        doctorName.setText(" ד\"ר " + doctor.getUserFirstName() + " " + doctor.getUserLastName() + " שלום,");

        submit = findViewById(R.id.button_DScheduleConfirm);
        goHome = findViewById(R.id.button_DScheduleReturn);
        spinnerChoose = findViewById(R.id.spinner_DSchedule);
        calendar = findViewById(R.id.calender_DSchedule);
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"יום עבודה מלא", "חצי יום"});
        spinnerChoose.setAdapter(adapter);
    }

    // Confirm the workday
    private void ConfirmWorkday() {
        // Get todays date and format it
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();

        // Check if date is picked
        if (date == null) {
            Log.d(TAG, "no date");
            Toast.makeText(SetAppointment.this, "No date picked",
                    Toast.LENGTH_SHORT).show();
        }
//        // TODO: need to check how if the day picked is before "today"
//        else if(date < today.toString() ){
//            Log.d(TAG, "Invalied date");
//            Toast.makeText(SetAppointment.this, "Invalied date",
//                    Toast.LENGTH_SHORT).show();
//        }
        else {
            // Connect to DB. Appointments -> DocID -> Date
            FirebaseDatabase fdb = FirebaseDatabase.getInstance();
            refdb = fdb.getReference().child("Appointments").child(doctor.getUserID()).child(dbdate);

            // Set appointments in every 30 min
            for (int i = 9; i <= 12; i++) {
                if (i == 9) {
                    first_apt = "0" + i + ":00";
                    second_apt = "0" + i + ":30";
                } else {
                    first_apt = i + ":00";
                    second_apt = i + ":30";
                }
                set_appointment(first_apt);
                set_appointment(second_apt);

            }
            // Check if it's full day
            if (spinnerChoose.getSelectedItem().toString().contains("מלא")) {
                for (int i = 13; i <= 16; i++) {
                    first_apt = i + ":00";
                    second_apt = i + ":30";
                    set_appointment(first_apt);
                    set_appointment(second_apt);
                }
            }
            Toast.makeText(getApplicationContext(), "הסידור התווסף בהצלחה", Toast.LENGTH_LONG).show();
        }

    }

    private void set_appointment(String time) {
        // Create an Appointment object with all the data
        Appointment e1 = new Appointment(
                date,
                time,
                doctor.getUserID(),
                doctor.getUserFirstName(),
                doctor.getUserLastName(),
                "",
                "",
                "",
                true);
        // Insert in to Appointments -> DocID -> Date -> Time
        // Check if faild to do so.
        refdb.child(first_apt).setValue(e1).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "problem inserting appointment " + time + " " + e);
                e.printStackTrace();
            }
        });
    }

    private void print(Object s) {
        System.out.println(s);
    }
}