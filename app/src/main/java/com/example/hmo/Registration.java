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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    private EditText userID, userFN, userLN, userEmail, userPass;
    private NewMember newmmbr;
    private FirebaseDatabase fdb ;
    private DatabaseReference refdb;
    private FirebaseAuth mAuth ;
    private static final String TAG = "MyActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        // Connect to DB
        fdb = FirebaseDatabase.getInstance();
        refdb =  fdb.getInstance().getReference("Users");
        // Connect to Authntication
        mAuth = FirebaseAuth.getInstance();

        //Get info from UI
        userID = findViewById(R.id.userID);
        userFN = findViewById(R.id.userFirstName);
        userLN = findViewById(R.id.userLastName);
        userEmail = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPassword);
        Button registerButton = findViewById(R.id.createAccountButton_1);

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
            newmmbr = new NewMember(localUserID, localUserFN, localUserLN, localUserEmail, localUserPass);
            mAuth.createUserWithEmailAndPassword(localUserEmail, localUserPass)
                    .addOnCompleteListener(this, task -> NewAuthUser(task));

        }
    }

    private void NewAuthUser(Task<AuthResult> task){
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = mAuth.getCurrentUser();
            newmmbr.setUid(user.getUid());
            refdb.child(newmmbr.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) { //That is the ID does not exist
                        insertNewMember(newmmbr, newmmbr.getUserID());

                    } else{

                        Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            Toast.makeText(Registration.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();

        }

    };

//    private void checkDB() {
//        refdb.child(newmmbr.getUserID()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() == null) { //That is the ID does not exist
//                    insertNewMember(newmmbr, newmmbr.getUserID());
//                } else
//                    Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void insertNewMember(NewMember member, String ID) {
        refdb.child(ID).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "נרשמת בהצלחה", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "משהו השתבש", Toast.LENGTH_LONG).show();
            }
        }) ;

    }
}