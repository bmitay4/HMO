package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReplyMassageActivity extends AppCompatActivity {
    private EditText subject, content;
    private Massages m=new Massages();
    private NewMember member;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        m=(Massages) getIntent().getSerializableExtra("msg");
        member = (NewMember) getIntent().getSerializableExtra("member");

        subject = findViewById(R.id.replySubject);
        content = findViewById(R.id.replyContent);
        Button replyButton = findViewById(R.id.sendButton);
        replyButton.setOnClickListener(v -> reply());
    }


    private void reply(){
        String localsubject = subject.getText().toString();
        String localcontent = content.getText().toString();

        if (localsubject.isEmpty())
            subject.setError("שדה חובה");
        else if (localcontent.isEmpty())
            content.setError("שדה חובה");
        else{
            FirebaseDatabase.getInstance().getReference("Massage").child(m.getfromID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Massages newM=new Massages(localsubject,localcontent,m.getToID(),m.getToName(),m.getfromID(),m.getFromName());
                    FirebaseDatabase.getInstance().getReference("Massage").child(m.getfromID()).setValue(newM);
                    startActivity(new Intent(getApplicationContext(), mailDocActicity.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}


