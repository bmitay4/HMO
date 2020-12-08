package com.example.hmo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MailUserActivity extends Activity {

    private ListView simpleList;
    private View decorView;
    private String [] msgs;
    private ArrayList<Message> msg_list;
    private NewMember member;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_user);

        member = (NewMember) getIntent().getSerializableExtra("member");
        simpleList = (ListView) findViewById(R.id.userMails);
        getMassages();

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowMassageActivity.class);
                intent.putExtra("msg",msg_list.get(position));
                intent.putExtra("member",member);
                startActivity(intent);
            }
        });

    }

    private void getMassages() {

        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        msg_list = new ArrayList<Message>();
        refdb.child("Massage").child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dates : snapshot.getChildren()) {
                    for(DataSnapshot docid : dates.getChildren()){
                        for(DataSnapshot times : docid.getChildren()){
                            System.out.println(times.getValue().toString());
                            Message temp = times.getValue(Message.class);
                            msg_list.add(temp);
                        }
                    }

                }
                String[] a = new String[msg_list.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה חדשה מ"+msg_list.get(i).getFromName();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MailUserActivity.this, android.R.layout.simple_list_item_1, a);
                simpleList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }
}


