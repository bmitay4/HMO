package com.example.hmo.Login_Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.Appointment_Doctors.DoctorAppointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.Message_Doctors.DocSendMessageActivity;
import com.example.hmo.Message_Doctors.MailDocActicity;
import com.example.hmo.R;
import com.example.hmo.Appointment_Doctors.SetAppointment;

public class DoctorLogin extends AppCompatActivity {
    private TextView doctorName;
    private Button DocSchedules, DocAppointment, DocSendmsg;
    private NewDoctor doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        setValues();

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
                Intent intent=new Intent(getApplicationContext(), DocSendMessageActivity.class);
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

        Button msg = findViewById(R.id.button_notifications);
        Intent intent=new Intent(getApplicationContext(), MailDocActicity.class);
        intent.putExtra("doctor",doctor);
        msg.setOnClickListener(v -> startActivity(intent));
    }
    private void setValues(){
        DocSchedules = findViewById(R.id.button_DocSchedules);
        doctorName = findViewById(R.id.txt_doctorFullName);
        DocSendmsg = findViewById(R.id.button_DocMail);
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        doctorName.setText(" ד\"ר " +doctor.getUserFirstName()+" "+doctor.getUserLastName());
        DocAppointment = findViewById(R.id.button_queues);
    }
}