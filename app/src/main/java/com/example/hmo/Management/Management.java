package com.example.hmo.Management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.Login_Screens.MainActivity;
import com.example.hmo.Panels_Screens.ClientsPanel;
import com.example.hmo.Panels_Screens.DoctorsPanel;
import com.example.hmo.R;

public class Management extends AppCompatActivity {
    Button Doctors_Panel, Clients_Panel, Queues_Panel, Messages_Panel, Drugs_Panel, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setValues();
        Doctors_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DoctorsPanel.class)));
        Clients_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ClientsPanel.class)));
        goBack.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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
        goBack = findViewById(R.id.Button_ManagementGoHome);
    }
}
