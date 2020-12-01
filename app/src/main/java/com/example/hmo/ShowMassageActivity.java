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
    private Massages m=(Massages) getIntent().getSerializableExtra("msg");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_massage);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        subjest = findViewById(R.id.subjectFrom);
        subjest.setText("נושא:"+m.getSubject());
        content = findViewById(R.id.contentFrom);
        content.setText(m.getContent());
        from = findViewById(R.id.sendFrom);
        from.setText("הודעה מ"+m.getFromName());

        Button replyButton = findViewById(R.id.reply);
        replyButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ReplyMassageActivity.class)));
    }
}


