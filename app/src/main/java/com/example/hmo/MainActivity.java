package com.example.hmo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText userID, userPassword;
    private ArrayList<Object> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);
        userID = findViewById(R.id.textBoxID);
        userPassword = findViewById(R.id.textBoxPass);

        registerButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Registration.class)));
        loginButton.setOnClickListener(v -> tryLogin());
    }

    private void tryLogin() {
        userInfo = new ArrayList<>();
        String localUserID = userID.getText().toString();
        String localUserPass = userPassword.getText().toString();

        if (localUserID.isEmpty())
            userID.setError("שדה חובה");
        else if (localUserPass.isEmpty())
            userPassword.setError("שדה חובה");
        else {
            FirebaseDatabase.getInstance().getReference("Users").child(localUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        userInfo.add(value.getValue());
                    }
                    if(dataSnapshot.getValue() == null || !userInfo.get(3).toString().equals(localUserPass))
                        Toast.makeText(getApplicationContext(), "Error! Incorrect ID or password", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(getApplicationContext(), "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                        forwardInfo(localUserID);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    private void forwardInfo(String ID){
        String localUserFirstName = userInfo.get(1).toString();
        String localUserLastName = userInfo.get(2).toString();
        String localUserEmail = userInfo.get(0).toString();

        Intent intent = new Intent(getApplicationContext(), MainNavigator.class);
        intent.putExtra("userID", ID);
        intent.putExtra("userFirstName", localUserFirstName);
        intent.putExtra("userLastName", localUserLastName);
        intent.putExtra("userEmail", localUserEmail);

        startActivity(intent);
    }
}