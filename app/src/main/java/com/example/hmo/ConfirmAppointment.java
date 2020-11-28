package com.example.hmo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmAppointment extends AppCompatActivity {

    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private NewMember user;
    private Appointment apt;
    private Button confirm;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);
        user = (NewMember) getIntent().getSerializableExtra("user");
        apt = (Appointment) getIntent().getSerializableExtra("picked_apt");
        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> ConfirmBooking());

    }

    private void ConfirmBooking() {
        // Changing appointments settings
        apt.setAvailable(false);
        apt.setUserID(user.getUserID());
        apt.setUserName(user.getUserFirstName());
        apt.setUserLastName(user.getUserLastName());

        // Set avilable to falls
        String date = apt.getDate().replace(".","");
        String time = apt.getTime();
        String docid = apt.getDocID();
        refdb.child("Appointments").child(docid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));
        // Book in user appointment
        String userid = user.getUserID();
        refdb.child("UserAppointments").child(userid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));;

        // Book in doc appointment
        refdb.child("DoctorAppointments").child(docid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));;


    }

    private void FaildSetValue(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
        Log.d(TAG, e.getMessage());
        e.printStackTrace();
    }
}