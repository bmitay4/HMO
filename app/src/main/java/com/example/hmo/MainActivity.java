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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText userID, userPassword,email;
    private FirebaseDatabase fdb ;
    private DatabaseReference refdb;
    private FirebaseAuth mAuth ;
    private NewMember dbmmber;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);
        Button teamButton = findViewById(R.id.Button_TeamLogin);

        // Go to management activitis
        teamButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Management.class)));

        //Get user's ID & password & mail.
        userID = findViewById(R.id.textBoxID);
        userPassword = findViewById(R.id.textBoxPass);
        Button forgotPass = findViewById(R.id.forgetPassButton);

//        forgotPass.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegistrationDoctors.class)));
        registerButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Registration.class)));
        loginButton.setOnClickListener(v -> tryLogin());
    }

    private void tryLogin() {
        // Connect to DB
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        // Connect to Authntication
        mAuth = FirebaseAuth.getInstance();

        // Get user ID & password from UI
        String localUserID = userID.getText().toString();
        String localUserPass = userPassword.getText().toString();

        if (localUserID.isEmpty())
            userID.setError("שדה חובה");
        else if (localUserPass.isEmpty())
            userPassword.setError("שדה חובה");
        else {
            FindUIDandLogin(localUserID,localUserPass);
        }
    }

    private void FindUIDandLogin(String localUserID, String localUserPass) {
        DatabaseReference userref = refdb.child("Users").child(localUserID);
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() == null) {
                    Toast.makeText(getApplicationContext(), "Error! Incorrect ID", Toast.LENGTH_LONG).show();
                    return;
                }

                dbmmber = snapshot.getValue(NewMember.class);
                if(!dbmmber.getUserPassword().equals(localUserPass) ){
                    Toast.makeText(getApplicationContext(), "Error! Incorrect Password", Toast.LENGTH_LONG).show();

                }
                else {
                    mAuth.signInWithEmailAndPassword(dbmmber.getUserEmail(), dbmmber.getUserPassword()).
                            addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(getApplicationContext(), "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                                        forwardInfo();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void forwardInfo(){

        Intent intent = new Intent(getApplicationContext(), MainNavigator.class);
        intent.putExtra("user",dbmmber);

//        intent.putExtra("userID", dbmmber.getUid());
//        intent.putExtra("userFirstName", dbmmber.getUserFirstName());
//        intent.putExtra("userLastName", dbmmber.getUserLastName());
//        intent.putExtra("userEmail", dbmmber.getUserEmail());
//        intent.putExtra("uid", dbmmber.getUid());
        startActivity(intent);
    }
}