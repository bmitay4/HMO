package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Management extends AppCompatActivity {
    Button Doctors_Panel, Clients_Panel, Queues_Panel, Messages_Panel, Drugs_Panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setValues();
        Doctors_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DoctorsPanel.class)));
        Clients_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ClientsPanel.class)));
//        Queues_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
//        Messages_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
//        Drugs_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
    }

    private void setValues() {
        Doctors_Panel = findViewById(R.id.Button_Doctors);
        Clients_Panel = findViewById(R.id.Button_Clients);
        Queues_Panel = findViewById(R.id.Button_Queues);
        Messages_Panel = findViewById(R.id.Button_Messages);
        Drugs_Panel = findViewById(R.id.Button_Drugs);
    }
}
