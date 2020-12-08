package com.example.hmo.Login_Screens;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.R;

public class MainLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(getIntent().getStringExtra("userID"));
        System.out.println(getIntent().getStringExtra("userFirstName"));
        System.out.println(getIntent().getStringExtra("userLastName"));
        System.out.println(getIntent().getStringExtra("userEmail"));

    }

}
