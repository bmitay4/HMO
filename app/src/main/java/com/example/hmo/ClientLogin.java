package com.example.hmo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ClientLogin extends AppCompatActivity {
    private TextView clientName,webURL;
    private NewMember member;
    private Button msg,queue,new_msg_to_doc, watchQueues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_client);
        setValues();

        // Check my msg's
        msg = findViewById(R.id.button_notifications);
        // Set a new appointment
        queue = findViewById(R.id.button_queues);
        // URL to health department web site
        webURL = findViewById(R.id.txt_GoToHealth);
        // Send a new msg to doctor
        new_msg_to_doc = findViewById(R.id.button_DocMail);

        webURL.setOnClickListener(view -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://datadashboard.health.gov.il/COVID-19/general"));
            startActivity(browser);
        });

        queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("user", member);
                startActivity(intent);
            }
        });

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), MailUserActivity.class);
                intent.putExtra("member",member);
                startActivity(intent);
            }
        });

        new_msg_to_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SendMassageActivity.class);
                intent.putExtra("member",member);
                startActivity(intent);
            }
        });

        watchQueues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserAppointment.class);
                intent.putExtra("member",member);
                startActivity(intent);
            }
        });
    }
    private void setValues(){
        member = (NewMember) getIntent().getSerializableExtra("member");
        clientName = findViewById(R.id.txt_doctorFullName);
        clientName.setText(member.getUserFirstName()+" "+member.getUserLastName());
        watchQueues = findViewById(R.id.button_CMedicalFile);
    }
}