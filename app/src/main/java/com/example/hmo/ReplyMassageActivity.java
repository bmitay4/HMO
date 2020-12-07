package com.example.hmo;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ReplyMassageActivity extends AppCompatActivity {
    private EditText subject, content;
    private Massages m;
    private NewMember member;
    private Button reply;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        m= (Massages) getIntent().getSerializableExtra("msg");
        member = (NewMember) getIntent().getSerializableExtra("member");

        subject = findViewById(R.id.replySubject);
        content = findViewById(R.id.replyContent);
        reply = findViewById(R.id.replybutton);

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reply();
            }
        });
    }


    private void reply(){
        String localsubject = subject.getText().toString();
        String localcontent = content.getText().toString();

        if (localsubject.isEmpty())
            subject.setError("שדה חובה");
        else if (localcontent.isEmpty())
            content.setError("שדה חובה");
        else{
            FirebaseDatabase.getInstance().getReference("Massage").child(m.getFromID()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String date_string = formatter_date.format(date);
                    String time_string = formatter_time.format(date);

                    Massages newM=new Massages(localsubject,localcontent,m.getToID(),m.getToName(),m.getFromID(),m.getFromName(),date_string,time_string,false);
                    FirebaseDatabase.getInstance().getReference("Massage").child(m.getFromID()).setValue(newM).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"ההודעה נשלחה" , Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), ClientLogin.class);
                            intent.putExtra("member",member);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"הודעה לא נשלחה" , Toast.LENGTH_LONG).show();
                            Log.d("DB set value problem:","Could not send the msg");
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"בעיה בהתחברות לשרת" , Toast.LENGTH_LONG).show();
                    Log.d("DB problem:","Could not connect to the server");
                }
            });
        }
    }
}



