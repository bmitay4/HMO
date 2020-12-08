package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ShowMassageActivity extends AppCompatActivity {
    private TextView subjest, content,from;
    private View decorView;
    private Message msg;
    private NewMember member;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);

        member = (NewMember)getIntent().getSerializableExtra("member");
        msg =(Message) getIntent().getSerializableExtra("msg");
        msg.setRead(true);
        subjest = findViewById(R.id.subjectFrom);
        subjest.setText("נושא:"+msg.getSubject());

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
}


