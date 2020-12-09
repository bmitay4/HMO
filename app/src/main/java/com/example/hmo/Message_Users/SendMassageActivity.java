package com.example.hmo.Message_Users;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.Login_Screens.ClientLogin;
import com.example.hmo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class SendMassageActivity extends AppCompatActivity {
    private String[] doctors;
    private EditText subject, content;
    private String doc, docID, hebrew_pick_doc;
    private ArrayList<NewDoctor> listingdocs;
    private NewMember member;
    private Spinner spin;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        hebrew_pick_doc = "בחר רופא";
        doc = hebrew_pick_doc;

        member = (NewMember) getIntent().getSerializableExtra("member");
        spin = (Spinner) findViewById(R.id.doctorsSpinner);
        getDoctor();

        subject = findViewById(R.id.subjectText);
        content = findViewById(R.id.contentText);
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> send());
    }

    public void getDoctor() {

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
                String[] a = new String[listingdocs.size() + 1];
                a[0] = hebrew_pick_doc;
                for (int i = 1; i < a.length; i++) {
                    a[i] = listingdocs.get(i - 1).getUserFirstName() + " " + listingdocs.get(i - 1).getUserLastName();
                }
                doctors = a;
                ArrayAdapter aa = new ArrayAdapter(SendMassageActivity.this, android.R.layout.simple_spinner_item, a);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int j = i + 1;
                        Toast.makeText(getApplicationContext(), doctors[i], Toast.LENGTH_LONG).show();
                        doc = doctors[i];
                        if (!doc.equals(hebrew_pick_doc)) {
                            docID = listingdocs.get(i - 1).getUserID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getApplicationContext(), hebrew_pick_doc, Toast.LENGTH_LONG).show();
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

    }

    private void send() {
        String localsubject = subject.getText().toString();
        String localcontent = content.getText().toString();

        if (localsubject.isEmpty())
            subject.setError("שדה חובה");
        else if (localcontent.isEmpty())
            content.setError("שדה חובה");
        else if (doc.equals(hebrew_pick_doc) || docID.isEmpty() )
            Toast.makeText(getApplicationContext(), "אנא בחר רופא", Toast.LENGTH_LONG).show();
        else {

            SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String date_msg = formatter_date.format(date);
            String time_msg = formatter_time.format(date);
            String date_db = date_msg.replace("/","");
            String time_db = time_msg.replace(":","");

            Message m = new Message(localsubject, localcontent, member.getUserID(), member.getUserFirstName() + " " + member.getUserLastName(), docID, doc, date_msg,time_msg,false);
            FirebaseDatabase.getInstance().getReference("Message").child(docID).child(date_db).child(member.getUserID()).child(time_db).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "ההודעה נשלחה", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), ClientLogin.class);
                    intent.putExtra("member", member);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "ההודעה לא נשלחה", Toast.LENGTH_LONG).show();
                    Log.d("Could not set value:", "sending Message to doctor failed " + e);
                }
            });
        }

    }


}


