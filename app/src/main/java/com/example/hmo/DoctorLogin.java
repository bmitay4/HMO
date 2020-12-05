package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorLogin extends AppCompatActivity {
    private TextView doctorName;
    private Button DocSchedules, DocAppointment;
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

        Button msg = findViewById(R.id.button_notifications);
        Intent intent=new Intent(getApplicationContext(), MailDocActicity.class);
        intent.putExtra("doctor",doctor);
        msg.setOnClickListener(v -> startActivity(intent));
    }
    private void setValues(){
        DocSchedules = findViewById(R.id.button_DocSchedules);
        doctorName = findViewById(R.id.txt_doctorFullName);
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        doctorName.setText(" ד\"ר " +doctor.getUserFirstName()+" "+doctor.getUserLastName());
        DocAppointment = findViewById(R.id.button_queues);
    }
}