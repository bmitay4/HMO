package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DocShowMassageActivity extends AppCompatActivity {
    private TextView subjest, content,from;
    private Massages m;
    private NewDoctor doctor;
    private FirebaseDatabase fr;
    private DatabaseReference refdb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);

        m=(Massages) getIntent().getSerializableExtra("msg");
        doctor= (NewDoctor) getIntent().getSerializableExtra("doctor");
        fr = FirebaseDatabase.getInstance();
        refdb = fr.getReference();

        subjest = findViewById(R.id.subjectFrom);
        subjest.setText("נושא:"+m.getSubject());
        content = findViewById(R.id.contentFrom);
        content.setText(m.getContent());
        from = findViewById(R.id.sendFrom);
        from.setText("הודעה מ"+m.getFromName());

        set_read_msg();

        Button replyButton = findViewById(R.id.reply);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), DocReplyMassageActivity.class);
                intent.putExtra("msg",m);
                intent.putExtra("doctor",doctor);
                startActivity(intent);
            }
        });

    }

    private void set_read_msg() {

        refdb.child(doctor.getUserID()).child(m.getDate()).child(m.getFromID()).child(m.getTime()).child("read").setValue(true);

    }
}

