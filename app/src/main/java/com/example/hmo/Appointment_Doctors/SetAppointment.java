package com.example.hmo.Appointment_Doctors;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.IntStream;

public class SetAppointment extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private Button submit, goHome;
    private Spinner spinnerChoose;
    private CalendarView calendar;
    private NewDoctor doctor;
    private DatabaseReference refdb, reference;
    private String date, dbdate, first_apt, second_apt;
    private String doctorPickedDate;
    private int scopeWorkSelected;
    private Context myContext;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        //Get the widgets from the layout and link them to the variables
        setValues();

        //Get the date selected by the user and formatted it according to DB structure
        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            //Get the date selected by the user
            selectedDate = LocalDate.of(year, Month.values()[month], dayOfMonth);

            //Re-format to display the date in day/month/year configuration
            doctorPickedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            //Remove the character '/' to fit DB structure
            doctorPickedDate = doctorPickedDate.replace("/", "");
        });
//        Old Version of setOnDateChangeListener, Get the date selected by the user
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
//                String int_day_tostring = "" + dayOfMonth;
//                String int_month_tostring = "" + (month + 1);
//
//                if (int_day_tostring.length() < 2) {
//                    date = "0" + dayOfMonth + ".";
//
//                } else {
//                    date = dayOfMonth + ".";
//                }
//                if (int_month_tostring.length() < 2) {
//                    date = date + "0" + (month + 1) + "." + year;
//                } else {
//                    date = date + (month + 1) + "." + year;
//                }
//                dbdate = date.replace(".", "");
//            }
//        });
        submit.setOnClickListener(v -> confirmWorkDay());
        goHome.setOnClickListener(v -> this.finish());
    }

    //Get the widgets from the layout and link them to the variables
    @SuppressLint("SetTextI18n")
    private void setValues() {
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        TextView doctorName = findViewById(R.id.txt_DSchedulesName);
        doctorName.setText(" ד\"ר " + doctor.getUserFirstName() + " " + doctor.getUserLastName() + " שלום,");

        submit = findViewById(R.id.button_DScheduleConfirm);
        goHome = findViewById(R.id.button_DScheduleReturn);
        spinnerChoose = findViewById(R.id.spinner_DSchedule);
        calendar = findViewById(R.id.calender_DSchedule);

        //Set Sunday as the first day of calendar
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"יום עבודה מלא", "חצי יום"});
        spinnerChoose.setAdapter(adapter);

        //Holds the context during operations in front of the DB
        myContext = this;
    }

    //Confirmation of the appointment after verifying that the values ​​are valid
    private void confirmWorkDay() {
        //Keep an instance and a reference to the DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Appointments").child(doctor.getUserID());

        //Get the user's choice for the scope of his work day, default is 0
        scopeWorkSelected = (int) spinnerChoose.getSelectedItemId();

        //Make sure the doctor has chosen a day, and that day has not been previously chosen by him
        if (doctorPickedDate == null)
            new AlertDialog.Builder(this)
                    .setTitle("שגיאה")
                    .setMessage("אנא בחר תאריך מבוקש לעבודה").show();
        else {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressWarnings("UnnecessaryReturnStatement")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(doctorPickedDate)) {
                        new AlertDialog.Builder(myContext)
                                .setTitle("שגיאה")
                                .setMessage("כבר קיים סידור עבודה ליום זה, אנא בחר יום אחר.").show();

                        //If your doctor has previously sent a work arrangement for this day, quit the function
                        return;
                    } else {
                        ArrayList<Integer> workHours = new ArrayList<>();

                        //Get the doctor's work hours according to his choice
                        if (scopeWorkSelected == 0)
                            workHours.addAll(Arrays.asList(9, 17));
                        else
                            workHours.addAll(Arrays.asList(9, 13));

                        IntStream.range(workHours.get(0), workHours.get(1)).forEach(hour -> {
                                    LocalTime selectedTime = LocalTime.of(hour, 0);
                                    LocalTime selectedTimeHalfHour = LocalTime.of(hour, 30);

                                    //Add the appointment to DB by the selected date and time
                                    createAppointment(selectedTime);
                                    createAppointment(selectedTimeHalfHour);
                                }
                        );
                        Toast.makeText(myContext, "הסידור התווסף בהצלחה", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(myContext, "התרחשה שגיאה, אנא נסה שנית", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //Format the time to the desired structure according to the DB and add the appointment
    private void createAppointment(LocalTime appointmentTime) {
        String fixedTime = appointmentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String fixedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        Appointment addedAppointment = new Appointment(
                fixedDate,
                fixedTime,
                doctor.getUserID(),
                doctor.getUserFirstName(),
                doctor.getUserLastName(),
                "",
                "",
                "",
                true);

        // If any error occurs during the procedure, notify your doctor
        reference.child(doctorPickedDate).child(fixedTime).setValue(addedAppointment).addOnFailureListener(e -> {
            Toast.makeText(myContext, "התרחשה שגיאה, אנא נסה שנית", Toast.LENGTH_LONG).show();
        });
    }

//      Old Version of workday confirmation
//    // Confirm the workday
//    private void ConfirmWorkday() {
//        // Get todays date and format it
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
//        Date today = new Date();
//
//        // Check if date is picked
//        if (date == null) {
//            Log.d(TAG, "no date");
//            Toast.makeText(SetAppointment.this, "No date picked",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            // Connect to DB. Appointments -> DocID -> Date
//            FirebaseDatabase fdb = FirebaseDatabase.getInstance();
//            refdb = fdb.getReference().child("Appointments").child(doctor.getUserID()).child(dbdate);
//
//            // Set appointments in every 30 min
//            for (int i = 9; i <= 12; i++) {
//                if (i == 9) {
//                    first_apt = "0" + i + ":00";
//                    second_apt = "0" + i + ":30";
//                } else {
//                    first_apt = i + ":00";
//                    second_apt = i + ":30";
//                }
//                set_appointment(first_apt);
//                set_appointment(second_apt);
//
//            }
//            // Check if it's full day
//            if (spinnerChoose.getSelectedItem().toString().contains("מלא")) {
//                for (int i = 13; i <= 16; i++) {
//                    first_apt = i + ":00";
//                    second_apt = i + ":30";
//                    set_appointment(first_apt);
//                    set_appointment(second_apt);
//                }
//            }
//            Toast.makeText(getApplicationContext(), "הסידור התווסף בהצלחה", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    private void set_appointment(String time) {
//        // Create an Appointment object with all the data
//        Appointment e1 = new Appointment(
//                date,
//                time,
//                doctor.getUserID(),
//                doctor.getUserFirstName(),
//                doctor.getUserLastName(),
//                "",
//                "",
//                "",
//                true);
//        // Insert in to Appointments -> DocID -> Date -> Time
//        // Check if faild to do so.
//        refdb.child(time).setValue(e1).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "problem inserting appointment " + time + " " + e);
//                e.printStackTrace();
//            }
//        });
//    }

    //Print method for debug purpose
    private void print(Object s) {
        System.out.println(s);
    }
}