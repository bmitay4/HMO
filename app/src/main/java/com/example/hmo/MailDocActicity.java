package com.example.hmo;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
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
import java.util.Date;

public class MailDocActicity extends AppCompatActivity {

    private ListView simpleList;
    private View decorView;
    private String [] msgs;
    private ArrayList <Massages> m;
    private NewDoctor doctor;
    FirebaseDatabase fdb;
    DatabaseReference refdb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_doc);
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        simpleList = (ListView) findViewById(R.id.allMsgs);
        getMassages();

//        simpleList = (ListView) findViewById(R.id.allMsgs);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgs);
//        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DocShowMassageActivity.class);
                intent.putExtra("msg",m.get(position));
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

//        Button newMassage = findViewById(R.id.newMassage);
//        newMassage.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), SendMassageActivity.class)));

    }
    private void getMassages() {

        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        m = new ArrayList<Massages>();

        refdb.child("Massage").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dates : snapshot.getChildren()) {
                    for(DataSnapshot user_id : dates.getChildren()){
                        for(DataSnapshot times : user_id.getChildren()){
                            System.out.println(times.getValue().toString());
                            Massages temp = times.getValue(Massages.class);
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



