package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MailDocActicity extends AppCompatActivity {

    private ListView simpleList;
    private View decorView;
    private String [] msgs;
    private ArrayList <Message> m;
    private NewDoctor doctor;
    FirebaseDatabase fdb;
    DatabaseReference refdb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_doc);

        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        simpleList = (ListView) findViewById(R.id.allMsgs);
        getMassages();

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DocShowMassageActivity.class);
                intent.putExtra("msg",m.get(position));
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

    }
    private void getMassages() {

        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        m = new ArrayList<Message>();

        refdb.child("Massage").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

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



