package com.example.hmo;

import android.content.Intent;
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

public class DocReplyMassageActivity extends AppCompatActivity {
    private EditText subject, content;
    private Massages m;
    private NewDoctor thisDoc;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        m=(Massages) getIntent().getSerializableExtra("msg");
        thisDoc = (NewDoctor) getIntent().getSerializableExtra("doctor");

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
                    FirebaseDatabase.getInstance().getReference("Massage").child(m.getfromID()).setValue(newM).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    });;

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



