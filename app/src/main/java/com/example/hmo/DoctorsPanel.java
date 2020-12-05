package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Objects;

public class DoctorsPanel extends AppCompatActivity {
    private Spinner spinner;
    private ImageView refresh, remove;
    private EditText doctorID, doctorFN, doctorLN, doctorEmail, doctorPass, doctorSpecs;
    private ArrayList<Object> userInfo;
    private Button createDoc, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_doctor);

        setValues();

        refresh.setOnClickListener(v -> getDoctors());
        createDoc.setOnClickListener(v -> tryRegister());
        goBack.setOnClickListener(v->finish());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                spinner.getSelectedItem().toString();
                System.out.println("Remove Doctor");
            }
        });

    }

    private void setValues() {
        refresh = findViewById(R.id.Button_MDocRefresh);
        spinner = findViewById(R.id.MDocSpinner);
        remove = findViewById(R.id.Button_MDocRemove);

        doctorID = findViewById(R.id.txt_MDocID);
        doctorFN = findViewById(R.id.txt_MDocFN);
        doctorLN = findViewById(R.id.txt_MDocLN);
        doctorEmail = findViewById(R.id.txt_MDocEmail);
        doctorPass = findViewById(R.id.txt_MDocsPassword);
        doctorSpecs = findViewById(R.id.txt_MDocSpecialization);
        createDoc = findViewById(R.id.Button_MDocsCreate);
        goBack = findViewById(R.id.Button_MDocsGoToHome);

        String[] items = new String[]{"Refresh DB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        spinner.setAdapter(adapter);
    }

    private void getDoctors() {
        userInfo = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    int token = Objects.requireNonNull(value.getValue()).toString().indexOf('=');
                    userInfo.add(value.getValue().toString().substring(token + 1));
                }
                if (userInfo.size() == 0)
                    setSpinner(new String[]{"No Doctors in DB"});
                else {
                    String[] items = new String[userInfo.size()];
                    for (int i = 0; i < userInfo.size(); i++) {
                        items[i] = userInfo.get(i).toString().replaceAll("[a-zA-Z=@.{}0-9,]", "");
                    }
                    setSpinner(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setSpinner(String[] list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }

    private void tryRegister() {
        String localDocID = doctorID.getText().toString();
        String localDocFN = doctorFN.getText().toString();
        String localDocLN = doctorLN.getText().toString();
        String localDocEmail = doctorEmail.getText().toString();
        String localDocPass = doctorPass.getText().toString();
        String localDocSpecs = doctorSpecs.getText().toString();
        NewDoctor doctor;


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Doctors");

        doctor = new NewDoctor("", localDocID, localDocFN, localDocLN, localDocEmail, localDocPass, localDocSpecs);
        mAuth.createUserWithEmailAndPassword(localDocEmail, localDocPass).
                addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        doctor.setAuthID(user.getUid());

                        reference.child(localDocID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Verify that ID does isn't exist
                                if (snapshot.getValue() == null) {
                                    reference.child(localDocID).setValue(doctor);
                                    Toast.makeText(getApplicationContext(), "הרופא נוסף בהצלחה", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), DoctorsPanel.class));
                                } else
                                    Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else
                        Toast.makeText(DoctorsPanel.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                });
    }
}