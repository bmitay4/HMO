package com.example.hmo.Message_Doctors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.Message_Users.MailUserActivity;
import com.example.hmo.Message_Users.SendMassageActivity;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MailDocActicity extends AppCompatActivity {

    private ListView simpleList;
    private ArrayList <Message> m;
    private NewDoctor doctor;
    FirebaseDatabase fdb;
    DatabaseReference refdb;
    private Button new_msg, archive_msg, back_home;
    private boolean isArchive = false;
    private SearchView doc_msg_search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_doc);

        setup();
        new_msg.setOnClickListener(v -> doc_new_msg());
        archive_msg.setOnClickListener(v -> set_button());
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DocShowMessageActivity.class);
                intent.putExtra("msg",m.get(position));
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

    }


    protected void onResume() {
        super.onResume();
        if(!isArchive){
            getMessage();
        }else{
            getArchiveMessage();
        }

    }

    private void setup() {
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        simpleList = (ListView) findViewById(R.id.allMsgs);
        new_msg = findViewById(R.id.Doc_Mail_newMassage);
        archive_msg = findViewById(R.id.DocMsg_New_Or_Archive);
        back_home = findViewById(R.id.Doc_Mail_Back);
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        m = new ArrayList<Message>();
    }
    private void doc_new_msg() {
        Intent i = new Intent(MailDocActicity.this, DocSendMessageActivity.class);
        i.putExtra("doctor", doctor);
        startActivity(i);
    }

    private void set_button() {
        if (!isArchive) {
            isArchive = true;
            archive_msg.setText("הודעות חדשות");
            getArchiveMessage();
        } else {
            isArchive = false;
            archive_msg.setText("הודעות ישנות");
            getMessage();
        }
    }

    private void getArchiveMessage() {


        refdb.child("MessageArchive").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for(DataSnapshot user_id : dates.getChildren()){
                        for(DataSnapshot times : user_id.getChildren()){
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            m.add(temp);
                        }
                    }

                }
                String[] a = new String[m.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה מ"+m.get(i).getFromName();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MailDocActicity.this, android.R.layout.simple_list_item_1, a);
                simpleList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void getMessage() {


        refdb.child("Message").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for(DataSnapshot user_id : dates.getChildren()){
                        for(DataSnapshot times : user_id.getChildren()){
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            m.add(temp);
                        }
                    }

                }
                String[] a = new String[m.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה חדשה מ"+m.get(i).getFromName();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MailDocActicity.this, android.R.layout.simple_list_item_1, a);
                simpleList.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }
}



