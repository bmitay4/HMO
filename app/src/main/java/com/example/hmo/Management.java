package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Management extends AppCompatActivity {
    Button Doctors_Panel, Clients_Panel, Queues_Panel, Messages_Panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setValues();
        Doctors_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DoctorsPanel.class)));
//        Clients_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
//        Queues_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
//        Messages_Panel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
    }
    private void setValues(){
        //Hide bottom android navigator
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Doctors_Panel = findViewById(R.id.Button_Doctors);
        Clients_Panel = findViewById(R.id.Button_Clients);
        Queues_Panel = findViewById(R.id.Button_Queues);
        Messages_Panel = findViewById(R.id.Button_Messages);
    }
}
