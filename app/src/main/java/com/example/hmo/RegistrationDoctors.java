//package com.example.hmo;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class RegistrationDoctors extends AppCompatActivity {
//    private EditText doctorID, doctorFN, doctorLN, doctorEmail, doctorPass, doctorSpecs;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.add_doctor);
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//        doctorID = findViewById(R.id.userID);
//        doctorFN = findViewById(R.id.userFirstName);
//        doctorLN = findViewById(R.id.userLastName);
//        doctorEmail = findViewById(R.id.userEmail);
//        doctorPass = findViewById(R.id.userPassword);
//        doctorSpecs = findViewById(R.id.Specs);
//
//        Button registerButton = findViewById(R.id.createAccountButton_1);
//
//        registerButton.setOnClickListener(v -> tryRegister());
//    }
//
//    private void tryRegister() {
//        String localDocID = doctorID.getText().toString();
//        String localDocFN = doctorFN.getText().toString();
//        String localDocLN = doctorLN.getText().toString();
//        String localDocEmail = doctorEmail.getText().toString();
//        String localDocPass = doctorPass.getText().toString();
//        String localDocSpecs = doctorSpecs.getText().toString();
//
//
//
//        FirebaseDatabase.getInstance().getReference("Doctors").child(localDocID).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.getValue() == null) { //That is the ID does not exist
//                        NewDoctor member = new NewDoctor(localDocFN, localDocLN, localDocEmail, localDocPass, localDocSpecs);
//                        insertNewMember(member, localDocID);
//                    } else
//                        Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//    private void insertNewMember(NewDoctor member, String ID) {
//        FirebaseDatabase.getInstance().getReference("Doctors").child(ID).setValue(member);
//        Toast.makeText(getApplicationContext(), "נרשמת בהצלחה", Toast.LENGTH_LONG).show();
//        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//    }
//}