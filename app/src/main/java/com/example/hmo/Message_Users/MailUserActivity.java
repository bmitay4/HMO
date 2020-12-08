package com.example.hmo.Message_Users;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MailUserActivity extends Activity {

    private ListView simpleList;
    private ArrayAdapter<String> arrayAdapter;
    private View decorView;
    private String[] msgs;
    private ArrayList<Message> msg_list;
    private NewMember member;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private Button new_msg, archive_msg, back_home;
    private boolean isArchive = false;
    private SearchView user_msg_search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_user);

        member = (NewMember) getIntent().getSerializableExtra("member");
        simpleList = (ListView) findViewById(R.id.userMails);
        new_msg = findViewById(R.id.Mail_User_newMassage);
        archive_msg = findViewById(R.id.Msg_New_Or_Archive);
        back_home = findViewById(R.id.Mail_User_Back);
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        user_msg_search = findViewById(R.id.User_Msg_Search);

        new_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MailUserActivity.this, SendMassageActivity.class);
                i.putExtra("member", member);
                startActivity(i);
            }
        });

        back_home.setOnClickListener(v -> finish());

        getMessage();

        user_msg_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                MailUserActivity.this.arrayAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                MailUserActivity.this.arrayAdapter.getFilter().filter(s);
                return false;
            }
        });


        archive_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

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

    private void getMessage() {

        msg_list = new ArrayList<Message>();
        refdb.child("Message").child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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

        msg_list = new ArrayList<Message>();
        refdb.child("MessageArchive").child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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


