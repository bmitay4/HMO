package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    private EditText userID, userFN, userLN, userEmail, userDOB, userPass;
    private Spinner userGender;
    private Button registerButton, goBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        setValues();

        registerButton.setOnClickListener(v -> tryRegister());
        goBack.setOnClickListener(v->finish());
    }

    private void setValues() {
        userID = findViewById(R.id.txt_RClientID);
        userFN = findViewById(R.id.txt_RClientFN);
        userLN = findViewById(R.id.txt_RClientLN);
        userEmail = findViewById(R.id.txt_RClientEmail);
        userPass = findViewById(R.id.txt_RClientPass);
        userDOB = findViewById(R.id.txt_RClientDOB);
        userGender = findViewById(R.id.spinner_userGender);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"בחר מין", "זכר", "נקבה"});
        userGender.setAdapter(adapter);
        registerButton = findViewById(R.id.Button_RClientRegister);
        goBack = findViewById(R.id.Button_RClientBackToHome);
    }

    private void tryRegister() {
        String localUserID = userID.getText().toString();
        String localUserFN = userFN.getText().toString();
        String localUserLN = userLN.getText().toString();
        String localUserEmail = userEmail.getText().toString();
        String localUserDBO = userDOB.getText().toString();
        String localUserPass = userPass.getText().toString();
        String localUserGender = userGender.getSelectedItem().toString();


        if (localUserID.isEmpty())
            userID.setError("שדה חובה");
        else if (localUserFN.isEmpty())
            userFN.setError("שדה חובה");
        else if (localUserLN.isEmpty())
            userLN.setError("שדה חובה");
        else if (localUserEmail.isEmpty())
            userEmail.setError("שדה חובה");
        else if (localUserDBO.isEmpty())
            userDOB.setError("שדה חובה");
        else if (localUserPass.isEmpty())
            userPass.setError("שדה חובה");
        else if (localUserGender.contains("מין"))
            System.out.println("No Gender");
        else {

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");
            NewMember member;
            member = new NewMember("", localUserID, localUserFN, localUserLN, localUserEmail, localUserPass, localUserDBO,localUserGender);

            mAuth.createUserWithEmailAndPassword(localUserEmail, localUserPass).
                    addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            member.setAuthID(user.getUid());

                            reference.child(member.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Verify that ID does isn't exist
                                    if (snapshot.getValue() == null) {
                                        reference.child(member.getUserID()).setValue(member);
                                        Toast.makeText(getApplicationContext(), "נרשמת בהצלחה", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else
                                        Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else
                            Toast.makeText(Registration.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

}