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

public class Registration extends AppCompatActivity {
    private EditText userID, userFN, userLN, userEmail, userPass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        userID = findViewById(R.id.userID);
        userFN = findViewById(R.id.userFirstName);
        userLN = findViewById(R.id.userLastName);
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPassword);
        Button registerButton = findViewById(R.id.createAccountButton);

        registerButton.setOnClickListener(v -> tryRegister());
    }

    private void tryRegister() {
        String localUserID = userID.getText().toString();
        String localUserFN = userFN.getText().toString();
        String localUserLN = userLN.getText().toString();
        String localUserEmail = userEmail.getText().toString();
        String localUserPass = userPass.getText().toString();

        if (localUserID.isEmpty())
            userID.setError("שדה חובה");
        else if (localUserFN.isEmpty())
            userFN.setError("שדה חובה");
        else if (localUserLN.isEmpty())
            userLN.setError("שדה חובה");
        else if (localUserEmail.isEmpty())
            userEmail.setError("שדה חובה");
        else if (localUserPass.isEmpty())
            userPass.setError("שדה חובה");
        else {
            FirebaseDatabase.getInstance().getReference("Users").child(localUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) { //That is the ID does not exist
                        NewMember member = new NewMember(localUserFN, localUserLN, localUserEmail, localUserPass);
                        insertNewMember(member, localUserID);
                    } else
                        Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void insertNewMember(NewMember member, String ID) {
        FirebaseDatabase.getInstance().getReference("Users").child(ID).setValue(member);
        Toast.makeText(getApplicationContext(), "נרשמת בהצלחה", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}