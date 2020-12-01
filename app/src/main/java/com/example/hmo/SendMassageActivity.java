package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendMassageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String[] doctors;
    private EditText subject, content;
    private String doc,docID;
    private ArrayList<NewDoctor> listingdocs;
    private NewMember member;
    private Spinner spin;

    public void getDoctor(){
        FirebaseDatabase fdb;
        DatabaseReference refdb;
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        refdb.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listingdocs = new ArrayList<NewDoctor>();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    NewDoctor d = doc.getValue(NewDoctor.class);
                    listingdocs.add(d);
                }
                String[] a = new String[listingdocs.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = listingdocs.get(i).getUserFirstName()+" "+listingdocs.get(i).getUserLastName();
                }
                doctors=a;
                spin.setOnItemSelectedListener(SendMassageActivity.this);
                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(SendMassageActivity.this,android.R.layout.simple_spinner_item,a);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spin.setAdapter(aa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        member = (NewMember) getIntent().getSerializableExtra("member");
        spin = (Spinner) findViewById(R.id.doctorsSpinner);
        getDoctor();
//        spin.setOnItemSelectedListener(this);
//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,doctors);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        spin.setAdapter(aa);

        subject = findViewById(R.id.subjectText);
        content = findViewById(R.id.contentText);
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> send());
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),doctors[position] , Toast.LENGTH_LONG).show();
        doc=doctors[position];
        docID=listingdocs.get(position).getUserID();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(getApplicationContext(),"בחר רופא" , Toast.LENGTH_LONG).show();
    }

    private void send(){
        String localsubject = subject.getText().toString();
        String localcontent = content.getText().toString();

        if (localsubject.isEmpty())
            subject.setError("שדה חובה");
        else if (localcontent.isEmpty())
            content.setError("שדה חובה");
        else if (docID.isEmpty())
            Toast.makeText(getApplicationContext(),"אנא בחר רופא" , Toast.LENGTH_LONG).show();
        else{
            FirebaseDatabase.getInstance().getReference("Massage").child(docID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Massages m=new Massages(localsubject,localcontent,member.getUserID(),member.getUserFirstName()+" "+member.getUserLastName(),docID,doc);
                    FirebaseDatabase.getInstance().getReference("Massage").child(docID).setValue(m);
                    Intent intent=new Intent(getApplicationContext(), MailUserActivity.class);
                    intent.putExtra("member",member);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }



}


