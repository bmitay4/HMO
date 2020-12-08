package com.example.hmo.Message_Users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ShowMassageActivity extends AppCompatActivity {
    private TextView subjest, content,from;
    private View decorView;
    private Message msg;
    private NewMember member;
    private FirebaseDatabase fr;
    private DatabaseReference refdb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);

        fr = FirebaseDatabase.getInstance();
        refdb = fr.getReference();
        member = (NewMember)getIntent().getSerializableExtra("member");
        msg =(Message) getIntent().getSerializableExtra("msg");
        msg.setRead(true);
        subjest = findViewById(R.id.subjectFrom);
        subjest.setText("נושא:"+msg.getSubject());

        set_read_msg();
        content = findViewById(R.id.contentFrom);
        content.setText(msg.getContent());
        from = findViewById(R.id.sendFrom);
        from.setText("הודעה מ"+msg.getFromName());

        Button replyButton = findViewById(R.id.reply);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowMassageActivity.this,ReplyMassageActivity.class);
                i.putExtra("msg",msg);
                i.putExtra("member",member);
                startActivity(i);
            }
        });
    }
    private void set_read_msg() {
        String date_db = msg.getDate().replace("/","");
        String time_db = msg.getTime().replace(":","");
        msg.setRead(true);
        refdb.child("Message").child(member.getUserID()).child(date_db).child(msg.getFromID()).child(time_db).removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Remove value:","Faild to remove value from DB "+e);
                e.printStackTrace();
            }
        });
        refdb.child("MessageArchive").child(member.getUserID()).child(date_db).child(msg.getFromID()).child(time_db).setValue(msg).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Add value:","Faild to add value to DB "+e);
                e.printStackTrace();
            }
        });
    }
}


