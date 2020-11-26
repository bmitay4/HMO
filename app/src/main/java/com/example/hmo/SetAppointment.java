package com.example.hmo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetAppointment extends AppCompatActivity {

    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private NewDoctor doc;
    private String date,dbdate, workday,first_apt,second_apt;
    private CalendarView cal;
    private CheckBox halfday, fullday;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);
        doc = (NewDoctor) getIntent().getSerializableExtra("doc");
        cal = findViewById(R.id.caldoc);
        halfday = findViewById(R.id.halfday);
        fullday = findViewById(R.id.fullday);
        confirm = findViewById(R.id.confirm_doc_workday);

        // Pick half or full day
        halfday.setOnClickListener(v -> halfday_onClick(v));
        fullday.setOnClickListener(v -> fullday_onClick(v));

        // Pick date
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date =dayOfMonth+"."+(month+1)+"."+year;
                dbdate = date.replace(".","");
            }
        });
        // Confirm picks
        confirm.setOnClickListener(v -> ConfirmWorkday(v));
    }

    // Check that both half and full are not picked
    private void halfday_onClick(View v) {
        if (fullday.isChecked()) {
            halfday.setChecked(false);
            workday = "full";
        } else {
            workday = "half";
        }
    }

    private void fullday_onClick(View v) {
        if (halfday.isChecked()) {
            fullday.setChecked(false);
            workday = "half";
        } else {
            workday = "full";
        }
    }

    // Confirm the workday
    private void ConfirmWorkday(View v) {
        // Get todays date and format it
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date today = new Date();

        // Check if date is picked
        if(date == null){
            Log.d(TAG, "no date");
            Toast.makeText(SetAppointment.this, "No date picked",
                    Toast.LENGTH_SHORT).show();
        }
        // TODO: need to check how if the day picked is before "today"
//        else if(date < today.toString() ){
//            Log.d(TAG, "Invalied date");
//            Toast.makeText(SetAppointment.this, "Invalied date",
//                    Toast.LENGTH_SHORT).show();
//        }
        else if( !halfday.isChecked() && !fullday.isChecked()){
            Log.d(TAG, "no workday");
            Toast.makeText(SetAppointment.this, "Pick half or full day",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            // Connect to DB. Appointments -> DocID -> Date
            fdb = FirebaseDatabase.getInstance();
            refdb = fdb.getReference().child("Appointments").child(doc.getUserID()).child(dbdate);

            // Set appointments in every 30 min
            for(int i = 9; i<12 ;i++ )
            {
                if(i == 9){
                    first_apt = "0"+i+":00";
                    second_apt = "0"+i+":30";
                }
                else{
                    first_apt = i+":00";
                    second_apt = i+":30";
                }
                set_appointment(first_apt);
                set_appointment(second_apt);

            }
            // Check if it's full day
            if(workday.equals("full")){
                for(int i = 13; i<16 ;i++ ){
                    first_apt = i+":00";
                    second_apt = i+":30";
                    set_appointment(first_apt);
                    set_appointment(second_apt);
                }
            }
            Toast.makeText(getApplicationContext(), "Date confirmed", Toast.LENGTH_LONG).show();
        }

    }

    private void set_appointment(String time){
        // Create an Appointment object with all the data
        Appointment e1 = new Appointment(
                date,
                time,
                doc.getUserID(),
                doc.getUserFirstName(),
                doc.getUserLastName(),
                "",
                "",
                "",
                true);
        // Insert in to Appointments -> DocID -> Date -> Time
        // Check if faild to do so.
        refdb.child(first_apt).setValue(e1).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "problem inserting appointment "+time+" "+e);
                e.printStackTrace();
            }
        });
    }
}