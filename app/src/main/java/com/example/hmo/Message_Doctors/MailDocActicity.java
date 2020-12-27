package com.example.hmo.Message_Doctors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private ArrayList<Message> m;
    private NewDoctor doctor;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private Button new_msg, back_home;
    private boolean isArchive = false;
    private TextView toolbar_txt;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_doc);

        setup();
        new_msg.setOnClickListener(v -> doc_new_msg());
        back_home.setOnClickListener(v -> finish());
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DocShowMessageActivity.class);
                intent.putExtra("msg", m.get(position));
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.archive_msg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.msg_achive:
                Toast.makeText(this, "ארכיון הודעות", Toast.LENGTH_SHORT).show();
                getArchiveMessage();
                isArchive = true;
                return true;
            case R.id.msg_new:
                Toast.makeText(this, "תיבת הודעות", Toast.LENGTH_SHORT).show();
                getMessage();
                isArchive = false;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        if (!isArchive) {
            getMessage();
        } else {
            getArchiveMessage();
        }

    }

    private void setup() {
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        simpleList = (ListView) findViewById(R.id.allMsgs);
        new_msg = findViewById(R.id.Doc_Mail_newMassage);
        back_home = findViewById(R.id.Doc_Mail_Back);
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        m = new ArrayList<Message>();
        toolbar = findViewById(R.id.msg_toolbar);
        toolbar_txt = findViewById(R.id.msg_toolbar_title);
        setSupportActionBar(toolbar);
    }

    private void doc_new_msg() {
        Intent i = new Intent(MailDocActicity.this, DocSendMessageActivity.class);
        i.putExtra("doctor", doctor);
        startActivity(i);
    }

//    private void set_button() {
//        if (!isArchive) {
//            isArchive = true;
//            archive_msg.setText("הודעות חדשות");
//            getArchiveMessage();
//        } else {
//            isArchive = false;
//            archive_msg.setText("הודעות ישנות");
//            getMessage();
//        }
//    }

    private void getArchiveMessage() {
        toolbar_txt.setText("הודעות ישנות");

        refdb.child("MessageArchive").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for (DataSnapshot user_id : dates.getChildren()) {
                        for (DataSnapshot times : user_id.getChildren()) {
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            m.add(temp);
                        }
                    }

                }
                String[] a = new String[m.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה מ" + m.get(i).getFromName();
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
        toolbar_txt.setText("הודעות חדשות");

        refdb.child("Message").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for (DataSnapshot user_id : dates.getChildren()) {
                        for (DataSnapshot times : user_id.getChildren()) {
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            m.add(temp);
                        }
                    }

                }
                String[] a = new String[m.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה חדשה מ" + m.get(i).getFromName();
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



