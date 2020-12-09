package com.example.hmo.Message_Doctors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DocShowMessageActivity extends AppCompatActivity {
    private TextView subjest, content, from;
    private Message m;
    private NewDoctor doctor;
    private FirebaseDatabase fr;
    private DatabaseReference refdb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);

        m = (Message) getIntent().getSerializableExtra("msg");
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        fr = FirebaseDatabase.getInstance();
        refdb = fr.getReference();

        subjest = findViewById(R.id.subjectFrom);
        subjest.setText("נושא:" + m.getSubject());
        content = findViewById(R.id.contentFrom);
        content.setText(m.getContent());
        from = findViewById(R.id.sendFrom);
        from.setText("הודעה מ" + m.getFromName());

        set_read_msg();

        Button replyButton = findViewById(R.id.reply);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DocReplyMessageActivity.class);
                intent.putExtra("msg", m);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
            }
        });

    }

    private void set_read_msg() {
        String date_db = m.getDate().replace("/","");
        String time_db = m.getTime().replace(":","");
        m.setRead(true);
        refdb.child("Message").child(doctor.getUserID()).child(date_db).child(m.getFromID()).child(time_db).child("read").removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Remove value:","Faild to remove value from DB "+e);
                e.printStackTrace();
            }
        });
        refdb.child("MessageArchive").child(doctor.getUserID()).child(date_db).child(m.getFromID()).child(time_db).setValue(m).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Add value:","Faild to add value to DB "+e);
                e.printStackTrace();
            }
        });
    }
}

