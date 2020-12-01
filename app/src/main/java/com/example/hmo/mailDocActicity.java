package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mailDocActicity extends AppCompatActivity {
    private ListView simpleList;
    private String [] msgs;
    private ArrayList <Massages> m;
    private NewDoctor doctor;


    private void getMassages() {
        FirebaseDatabase fdb;
        DatabaseReference refdb;
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        refdb.child("Massages").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                m = new ArrayList<Massages>();
                for (DataSnapshot mes : snapshot.getChildren()) {
                    Massages temp = mes.getValue(Massages.class);
                    m.add(temp);
                }
                String[] a = new String[m.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = "הודעה חדשה מ"+m.get(i).getFromName();
                }
                msgs=a;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_doc);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        getMassages();

        simpleList = (ListView) findViewById(R.id.allMsgs);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgs);
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShowMassageActivity.class);
                intent.putExtra("msg",m.get(position));
                startActivity(intent);
            }
        });

        Button newMassage = findViewById(R.id.newMassage);
        newMassage.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), SendMassageActivity.class)));

    }
}



