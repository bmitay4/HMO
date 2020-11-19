package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DoctorsPanel extends AppCompatActivity {
    private Spinner spinner;
    private ImageView refresh;
    private EditText doctorID, doctorFN, doctorLN, doctorEmail, doctorPass, doctorSpecs;
    private ArrayList<Object> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_panel);

        TextView mTextView = findViewById(R.id.Text_Title);
        refresh = findViewById(R.id.refresh);
        spinner = findViewById(R.id.Spinner);
        String[] items = new String[]{"Refresh DB"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        refresh.setOnClickListener(v -> getDoctors());

        doctorID = findViewById(R.id.userID2);
        doctorFN = findViewById(R.id.userFirstName2);
        doctorLN = findViewById(R.id.userLastName2);
        doctorEmail = findViewById(R.id.userEmail2);
        doctorPass = findViewById(R.id.userPassword2);
        doctorSpecs = findViewById(R.id.specs);

        Button registerButton = findViewById(R.id.createAccountButton_);

        registerButton.setOnClickListener(v -> tryRegister());

    }

    private void getDoctors() {
        userInfo = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    int token = Objects.requireNonNull(value.getValue()).toString().indexOf('=');
                    userInfo.add(value.getValue().toString().substring(token+1));
                }
                if (userInfo.size() == 0)
                    setSpinner(new String[]{"No Doctors in DB"});
                else{
                    String[] items = new String[userInfo.size()];
                    for (int i = 0; i < userInfo.size(); i++) {
                        int token = userInfo.get(i).toString().indexOf('=');
//                        items[i] = userInfo.get(i).toString().substring(token);
                        items[i] = userInfo.get(i).toString().replaceAll("[a-zA-Z=@.{}0-9,]", "");

                        System.out.println(items[i]);

                    }
                    setSpinner(items);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void setSpinner(String[] list){
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



        FirebaseDatabase.getInstance().getReference("Doctors").child(localDocID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) { //That is the ID does not exist
                    NewDoctor member = new NewDoctor(localDocFN, localDocLN, localDocEmail, localDocPass, localDocSpecs);
                    insertNewMember(member, localDocID);
                } else
                    Toast.makeText(getApplicationContext(), "Error! ID already exist", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertNewMember(NewDoctor member, String ID) {
        FirebaseDatabase.getInstance().getReference("Doctors").child(ID).setValue(member);
        Toast.makeText(getApplicationContext(), "הרופא התווסף בהצלחה", Toast.LENGTH_LONG).show();
        startActivity(new Intent(getApplicationContext(), Management.class));
    }
}