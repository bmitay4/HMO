package com.example.hmo.Appointment_User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.Login_Screens.ClientLogin;
import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmAppointment extends AppCompatActivity {

    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private NewMember user;
    private Appointment apt;
    private Button confirmButton, goBackButton, goHomeButton;
    private TextView userName, appointmentInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_appointment);

        setValues();
        goBackButton.setOnClickListener(v->finish());
        goHomeButton.setOnClickListener(v->goHome());
        confirmButton.setOnClickListener(v -> ConfirmBooking());

    }
    private void goHome(){
        Intent intent = new Intent(getApplicationContext(), ClientLogin.class);
        //Will clear all opened activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("member", user);
        startActivity(intent);
    }
    private void setValues(){
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        user = (NewMember) getIntent().getSerializableExtra("user");
        apt = (Appointment) getIntent().getSerializableExtra("picked_apt");
        confirmButton = findViewById(R.id.Button_ConfirmConf);
        goBackButton = findViewById(R.id.Button_ConfirmGoBack);
        goHomeButton = findViewById(R.id.Button_ConfirmGoToHome);
        userName = findViewById(R.id.txt_ConfirmUserName);
        appointmentInfo = findViewById(R.id.txt_ConfirmAppointmentInfo);
        userName.setText(user.getUserFirstName()+" "+user.getUserLastName()+" כמעט סיימנו,");
        appointmentInfo.setText(apt.toString(user));
    }

    private void ConfirmBooking() {
        // Changing appointments settings
        apt.setAvailable(false);
        apt.setUserID(user.getUserID());
        apt.setUserName(user.getUserFirstName());
        apt.setUserLastName(user.getUserLastName());

        // Set available to falls
        String date = apt.getDate().replace(".","");
        String time = apt.getTime();
        String docid = apt.getDocID();

        refdb.child("Appointments").child(docid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));
        // Book in user appointment

        String userid = user.getUserID();
        refdb.child("UserAppointments").child(userid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));;

        // Book in doc appointment
        refdb.child("DoctorAppointments").child(docid).child(date).child(time).setValue(apt).addOnFailureListener(v -> FaildSetValue(v));;

        Toast.makeText(this, "התור נקבע בהצלחה!", Toast.LENGTH_LONG).show();
        goHome();
    }

    private void FaildSetValue(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(), "Somthing went wrong", Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }
}