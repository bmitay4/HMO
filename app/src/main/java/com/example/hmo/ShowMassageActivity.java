package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ShowMassageActivity extends AppCompatActivity {
    private TextView subjest, content,from;
    private View decorView;
    private Massages msg;
    private NewMember member;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        member = (NewMember)getIntent().getSerializableExtra("member");
        msg =(Massages) getIntent().getSerializableExtra("msg");
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
            }
        });
    }
}


