package com.example.hmo.Login_Screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.General_Objects.UserLoginDialog;
import com.example.hmo.Message_Users.MailUserActivity;
import com.example.hmo.R;
import com.example.hmo.Message_Users.SendMassageActivity;
import com.example.hmo.Appointment_User.UserAppointments;
import com.example.hmo.Appointment_User.BookAppointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClientLogin extends AppCompatActivity {
    private TextView clientName,webURL;
    private NewMember member;
    private Button msg,queue,new_msg_to_doc, watchQueues;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Context myContext;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_client);

        //Get the widgets from the layout and link them to the variables
        setValues();
        openDialog();

        // Check my msg's
        msg = findViewById(R.id.button_notifications);
        // Set a new appointment
        queue = findViewById(R.id.button_queues);
        // URL to health department web site
        webURL = findViewById(R.id.txt_GoToHealth);
        // Send a new msg to doctor
        new_msg_to_doc = findViewById(R.id.button_DocMail);

        //Listens to the messaging DB branch, and if the client receives any message from a doctor
        //then he gets alerted about it
        reference.child(member.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot member_id) {
//                int count = 0;
                for (DataSnapshot dates : member_id.getChildren()) {
                    for (DataSnapshot doctor_id : dates.getChildren()) {
                        for (DataSnapshot time : doctor_id.getChildren()) {
                            if (time.getValue(Message.class) != null && time.getValue(Message.class).getToID().equals(member.getUserID()))
                                count++;
                        }
                    }
                }
                if (count > 0)
                    Toast.makeText(myContext, "יש לך " + count + " הודעות חדשות שטרם נקראו", Toast.LENGTH_LONG);
//                            .show();
                //Due to technical issues, the message showing the amount of new messages has been hidden from the user
                count = 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                Intent intent=new Intent(getApplicationContext(), UserAppointments.class);
                intent.putExtra("member",member);
                startActivity(intent);
            }
        });
    }

    //Get the widgets from the layout and link them to the variables
    private void setValues(){
        member = (NewMember) getIntent().getSerializableExtra("member");
        clientName = findViewById(R.id.txt_doctorFullName);
        clientName.setText(member.getUserFirstName()+" "+member.getUserLastName());
        watchQueues = findViewById(R.id.button_CMedicalFile);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Message");

        //Holds the context during operations in front of the DB
        myContext = this;
    }

    public void openDialog(){
        UserLoginDialog d = new UserLoginDialog();
        d.show(getSupportFragmentManager(),"התראת חיסון");
    }
}