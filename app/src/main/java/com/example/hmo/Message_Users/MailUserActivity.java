package com.example.hmo.Message_Users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MailUserActivity extends AppCompatActivity {

    private ListView simpleList;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Message> msg_list;
    private NewMember member;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private Button new_msg, back_home;
    private boolean isArchive = false;
    private TextView toolbar_txt;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_user);


        setup();
        new_msg.setOnClickListener(v-> user_new_msg());
        back_home.setOnClickListener(v -> finish());

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowMassageActivity.class);
                intent.putExtra("msg", msg_list.get(position));
                intent.putExtra("member", member);
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


    private void user_new_msg() {
        Intent i = new Intent(MailUserActivity.this, SendMassageActivity.class);
        i.putExtra("member", member);
        startActivity(i);
    }

    private void setup() {
        member = (NewMember) getIntent().getSerializableExtra("member");
        simpleList = (ListView) findViewById(R.id.userMails);
        new_msg = findViewById(R.id.Mail_User_newMassage);
        back_home = findViewById(R.id.Mail_User_Back);
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();

        toolbar = findViewById(R.id.msg_toolbar_user);
        toolbar_txt = findViewById(R.id.msg_toolbar_user_title);
        setSupportActionBar(toolbar);
    }


    private void getMessage() {
        toolbar_txt.setText("הודעות חדשות");

        refdb.child("Message").child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msg_list = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for (DataSnapshot docid : dates.getChildren()) {
                        for (DataSnapshot times : docid.getChildren()) {

                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            if(!temp.getRead())
                                msg_list.add(temp);
                        }
                    }
                }
                String[] a = new String[msg_list.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה חדשה מ " + msg_list.get(i).getFromName() + " " + msg_list.get(i).getSubject();
                }
                arrayAdapter = new ArrayAdapter<String>(MailUserActivity.this, android.R.layout.simple_list_item_1, a);
                simpleList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void getArchiveMessage() {

        toolbar_txt.setText("הודעות ישנות");
        refdb.child("MessageArchive").child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msg_list = new ArrayList<Message>();
                for (DataSnapshot dates : snapshot.getChildren()) {
                    for (DataSnapshot docid : dates.getChildren()) {
                        for (DataSnapshot times : docid.getChildren()) {
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            if(temp.getRead())
                                msg_list.add(temp);
                        }
                    }
                }
                String[] a = new String[msg_list.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה מ" + msg_list.get(i).getFromName() + " " + msg_list.get(i).getSubject();
                }
                arrayAdapter = new ArrayAdapter<String>(MailUserActivity.this, android.R.layout.simple_list_item_1, a);
                simpleList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

}


