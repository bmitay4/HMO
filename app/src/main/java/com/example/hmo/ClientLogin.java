package com.example.hmo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ClientLogin extends AppCompatActivity {
    private TextView clientName;
    private NewMember member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_client);
        setValues();
        TextView webURL = findViewById(R.id.txt_GoToHealth);
        webURL.setOnClickListener(view -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://datadashboard.health.gov.il/COVID-19/general"));
            startActivity(browser);
        });
    }
    private void setValues(){
        clientName = findViewById(R.id.txt_doctorFullName);
        member = (NewMember) getIntent().getSerializableExtra("member");
        clientName.setText(member.getUserFirstName()+" "+member.getUserLastName());
    }
}