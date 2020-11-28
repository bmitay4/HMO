package com.example.hmo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorLogin extends AppCompatActivity {
    private TextView doctorName;
    private NewDoctor doctor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);
        setValues();
    }
    private void setValues(){
        doctorName = findViewById(R.id.txt_doctorFullName);
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        doctorName.setText(" ד\"ר " +doctor.getUserFirstName()+" "+doctor.getUserLastName());
    }
}