package com.example.hmo.Message_Doctors;

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

import com.example.hmo.General_Objects.Message;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class DocReplyMessageActivity extends AppCompatActivity {
    private EditText subject, content;
    private Message m;
    private NewDoctor thisDoc;
    private Button reply;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        m=(Message) getIntent().getSerializableExtra("msg");
        thisDoc = (NewDoctor) getIntent().getSerializableExtra("doctor");

        subject = findViewById(R.id.replySubject);
        content = findViewById(R.id.replyContent);
        subject.setText("reply:"+m.getSubject());
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
            SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter_time = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String date_msg = formatter_date.format(date);
            String time_msg = formatter_time.format(date);
            String date_db = date_msg.replace("/","");
            String time_db = time_msg.replace(":","");

            Message newM=new Message(localsubject,localcontent,m.getToID(),m.getToName(),m.getFromID(),m.getFromName(),date_msg,time_msg,false);
            FirebaseDatabase.getInstance().getReference("Message").child(newM.getToID()).child(date_db).child(thisDoc.getUserID()).child(time_db).setValue(newM).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"ההודעה נשלחה" , Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), MailDocActicity.class);
                    intent.putExtra("doctor",thisDoc);
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
    }
}



