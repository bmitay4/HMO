package com.example.hmo.Login_Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.hmo.Appointment_Doctors.DoctorAppointment;
import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.Message_Doctors.DocSendMessageActivity;
import com.example.hmo.Message_Doctors.MailDocActicity;
import com.example.hmo.R;
import com.example.hmo.Appointment_Doctors.SetAppointment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorLogin extends AppCompatActivity {
    private TextView doctorName;
    private Button DocSchedules, DocAppointment, DocSendmsg;
    private NewDoctor doctor;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        //Get the widgets from the layout and link them to the variables
        setValues();

        //Listens to the messaging DB branch, and if the doctor receives any message from a client
        //then he gets alerted about it
        reference.child(doctor.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot doctor_id) {
                int count = 0;
                for (DataSnapshot dates : doctor_id.getChildren()) {
                    for (DataSnapshot client_id : dates.getChildren()) {
                        for (DataSnapshot time : client_id.getChildren()) {
                            if (time.getValue(Message.class) != null)
                                count++;
                        }
                    }
                }
                if (count > 0)
                    Toast.makeText(myContext, "יש לך " + count + " הודעות חדשות שטרם נקראו", Toast.LENGTH_LONG)
                            .show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DocSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SetAppointment.class);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }
        });
        DocAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DoctorAppointment.class);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }
        });

        DocSendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DocSendMessageActivity.class);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }
        });

        Button msg = findViewById(R.id.button_notifications);
        Intent intent = new Intent(getApplicationContext(), MailDocActicity.class);
        intent.putExtra("doctor", doctor);
        msg.setOnClickListener(v -> startActivity(intent));
    }

    //Get the widgets from the layout and link them to the variables
    @SuppressLint("SetTextI18n")
    private void setValues() {
        DocSchedules = findViewById(R.id.button_DocSchedules);
        doctorName = findViewById(R.id.txt_doctorFullName);
        DocSendmsg = findViewById(R.id.button_DocMail);
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        doctorName.setText(" ד\"ר " + doctor.getUserFirstName() + " " + doctor.getUserLastName());
        DocAppointment = findViewById(R.id.button_queues);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Message");

        //Holds the context during operations in front of the DB
        myContext = this;
    }

    //Print function, for debug purpose
    private void print(Object s) {
        System.out.println(s);
    }
}