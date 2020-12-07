package com.example.hmo;

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

public class DocSendMassageActivity extends AppCompatActivity {
    private String[] users;
    private EditText subject, content;
    private String userName, userID, hebrew_pick_user;
    private ArrayList<NewMember> listinguser;
    private NewDoctor thisDoc;
    private Spinner spin;
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        hebrew_pick_user = "בחר מטופל";
        userName = hebrew_pick_user;

        thisDoc = (NewDoctor) getIntent().getSerializableExtra("doctor");
        spin = (Spinner) findViewById(R.id.doctorsSpinner);
        getUsers();

        subject = findViewById(R.id.subjectText);
        content = findViewById(R.id.contentText);
        Button sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> send());
    }

    public void getUsers() {
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        refdb.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listinguser = new ArrayList<NewMember>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NewMember u = data.getValue(NewMember.class);
                    listinguser.add(u);
                }
                String[] a = new String[listinguser.size() + 1];
                a[0] = hebrew_pick_user;
                for (int i = 0; i < a.length; i++) {
                    a[i] = listinguser.get(i - 1).getUserFirstName() + " " + listinguser.get(i - 1).getUserLastName();
                }
                users = a;
                ArrayAdapter aa = new ArrayAdapter(DocSendMassageActivity.this, android.R.layout.simple_spinner_item, a);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getApplicationContext(), users[i], Toast.LENGTH_LONG).show();
                        userName = users[i];
                        if (!userName.equals(hebrew_pick_user)) {
                            userID = listinguser.get(i - 1).getUserID();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Toast.makeText(getApplicationContext(), hebrew_pick_user, Toast.LENGTH_LONG).show();
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
        else if (userID.isEmpty() || userName.equals(hebrew_pick_user))
            Toast.makeText(getApplicationContext(), "אנא בחר מטופל", Toast.LENGTH_LONG).show();
        else {
            refdb.child("Massage").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String date_string = formatter_date.format(date);
                    String time_string = formatter_time.format(date);

                    Massages m = new Massages(localsubject, localcontent, thisDoc.getUserID(), thisDoc.getUserFirstName() + " " + thisDoc.getUserLastName(), userID, userName,date_string,time_string,false);
                    FirebaseDatabase.getInstance().getReference("Massage").child(userID).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "ההודעה נשלחה", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), DoctorLogin.class);
                            intent.putExtra("doctor", thisDoc);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "ההודעה לא נשלחה", Toast.LENGTH_LONG).show();
                            Log.d("Could not set value:", "sending messge to user failed " + e);
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}
