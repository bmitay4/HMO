package com.example.hmo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button msg = findViewById(R.id.button_DocMail);
        Button queue = findViewById(R.id.button_queues);
        queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("user", member);
                startActivity(intent);
            }
        });

        Intent intent=new Intent(getApplicationContext(), MailUserActivity.class);
        intent.putExtra("member",member);

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
    private void setValues(){
        clientName = findViewById(R.id.txt_doctorFullName);
        member = (NewMember) getIntent().getSerializableExtra("member");
        clientName.setText(member.getUserFirstName()+" "+member.getUserLastName());
    }
}