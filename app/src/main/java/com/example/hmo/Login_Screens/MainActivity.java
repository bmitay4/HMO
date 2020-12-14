package com.example.hmo.Login_Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Admin;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.Management.Management;
import com.example.hmo.OnGetDataListener;
import com.example.hmo.R;
import com.example.hmo.Registration.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase fr;
    private DatabaseReference refdb;
    private Button registerButton, loginButton, teamButton, forgotPass;
    private EditText userID, userPassword;
    private NewMember member;
    private NewDoctor doctor;
    private int userType = -1;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setValues();

        teamButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Management.class)));
        registerButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Registration.class)));
        loginButton.setOnClickListener(v -> login());
    }

    private void setValues() {
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        teamButton = findViewById(R.id.Button_TeamLogin);
        forgotPass = findViewById(R.id.forgetPassButton);
        userID = findViewById(R.id.textBoxID);
        userPassword = findViewById(R.id.textBoxPass);

        //Holds the context during operations in front of the DB
        myContext = this;
    }

    private void login(){
        String localUserID = userID.getText().toString();
        String localUserPass = userPassword.getText().toString();

        if(!checkFields(localUserID, localUserPass)){ return;}
        fr = FirebaseDatabase.getInstance();
        refdb = fr.getReference();
        DatabaseReference ref_user = refdb.child("Users");
        DatabaseReference ref_doc = refdb.child("Doctors");
        DatabaseReference admin_reference = refdb.child("Admins");

        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                member = (NewMember) snapshot.child(localUserID).getValue(NewMember.class);
                if(member == null){ return;}
                else if(!member.getUserPassword().equals(localUserPass) )
                {
                    Toast.makeText(MainActivity.this, "סיסמה לא נכונה", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ClientLogin.class);
                intent.putExtra("member", member);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "DB-User failed", Toast.LENGTH_SHORT).show();
            }
        });
        ref_doc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctor = (NewDoctor) snapshot.child(localUserID).getValue(NewDoctor.class);
                if(doctor == null){ return;}
                else if(!doctor.getUserPassword().equals(localUserPass) )
                {
                    Toast.makeText(MainActivity.this, "סיסמה לא נכונה", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), DoctorLogin.class);
                intent.putExtra("doctor", doctor);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Check if the user is an admin, and if so transfer it to the management system
        admin_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin admin = (Admin) snapshot.child(localUserID).getValue(Admin.class);

                if(admin != null && admin.getAdminPassword().equals(localUserPass)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                    builder.setTitle("התחברות מנהל");
                    builder.setMessage("הנך נכנס לאזור רגיש המכיל מידע אישי, האם להמשיך?");
                    builder.setPositiveButton("כן", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(myContext, Management.class);
                        startActivity(intent);
                        finish();
                    });

                    builder.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "DB-User failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void tryLogin() {
        // Get user ID & password
        String localUserID = userID.getText().toString();
        String localUserPass = userPassword.getText().toString();

        if (checkFields(localUserID, localUserPass)) {
            verifyUserType(localUserID);
            if (userType == 0) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(localUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        member = snapshot.getValue(NewMember.class);
                        if (member == null || !member.getUserPassword().equals(localUserPass)) {
                            Toast.makeText(getBaseContext(), "Error! Incorrect ID or Password", Toast.LENGTH_LONG).show();
                        } else {
                            // Connect to Authentication
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(member.getUserEmail(), member.getUserPassword()).
                                    addOnCompleteListener(MainActivity.this, task -> {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(MainActivity.this, "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), ClientLogin.class);
                                            intent.putExtra("member", member);
                                            startActivity(intent);

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (userType == 1) {
                FirebaseDatabase.getInstance().getReference().child("Doctors").child(localUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctor = snapshot.getValue(NewDoctor.class);
                        if (doctor == null || !doctor.getUserPassword().equals(localUserPass)) {
                            Toast.makeText(MainActivity.this, "Error! Incorrect Password", Toast.LENGTH_LONG).show();
                        } else {
                            // Connect to Authentication
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(doctor.getUserEmail(), doctor.getUserPassword()).
                                    addOnCompleteListener(MainActivity.this, task -> {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(MainActivity.this, "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), DoctorLogin.class);
                                            intent.putExtra("doctor", doctor);
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    //Checks if the givenID is member's or doctor's
//    private int getUserType(String givenID) {
//        System.out.println(2);
//
//        reference.child("Users").child(givenID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    System.out.println(snapshot.child(givenID).toString());
//                    userType = 0;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        reference.child("Doctors").child(givenID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists())
//                    userType = 1;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        return userType;
//    }

    private boolean checkFields(String localUserID, String localUserPass) {
        if (localUserID.isEmpty())
            userID.setError("שדה חובה");
        else if (localUserPass.isEmpty())
            userPassword.setError("שדה חובה");
        else return true;

        return false;
    }
    private void verifyUserType(String ID){
        print("in user type");
        isClient(ID);
        isDoctor(ID);
    }
    private void isClient(String ID) {
        print("client is looking for info at db");
        readData(FirebaseDatabase.getInstance().getReference().child("Users").child(ID), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userType = 0;
                    System.out.println(dataSnapshot.child(ID).toString());
                }
            }
            @Override
            public void onStart() {
                //when starting
                Log.d("ONSTART", "Started");
            }
            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });
    }

    private void isDoctor(String ID) {
        print("doctor is looking for info at db");
        readData(FirebaseDatabase.getInstance().getReference().child("Doctors").child(ID), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userType = 1;
                    System.out.println(dataSnapshot.child(ID).toString());
                }
            }
            @Override
            public void onStart() {
                //when starting
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });
    }
    public void readData(DatabaseReference reference, final OnGetDataListener listener) {
        listener.onStart();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });

    }
    private void print(Object s){
        System.out.println(s);
    }
}