package com.example.hmo.Panels_Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.R;
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
    //    private ImageView refresh, remove;
    private TextView doctorInfo;
    private NewDoctor doctor;
    private ArrayList<NewDoctor> doctorsList;
    private DatabaseReference reference;
    private EditText doctorID, doctorFN, doctorLN, doctorEmail, doctorPass, doctorSpecs;
    //    private ArrayList<Object> userInfo;
    private Button createDoc, goBack, refresh, remove;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_doctor);

        //Get the widgets from the layout and link them to the variables
        setValues();

        refresh.setOnClickListener(v -> getDoctors());
        createDoc.setOnClickListener(v -> tryRegister());
        goBack.setOnClickListener(v -> finish());
        remove.setOnClickListener(v -> {
            doctor = doctorsList.get(spinner.getSelectedItemPosition());
            if (doctor != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("אזהרה");
                builder.setMessage("האם אתה בטוח שברצונך להסיר את דוקטור "
                        + doctor.getUserFirstName() + " " + doctor.getUserLastName() +
                        " מהמאגר? אין אפשרות לבטל פעולה זו");

                builder.setPositiveButton("כן, מחק", (dialog, which) -> {
                    remove(doctor);
                    dialog.dismiss();
                });

                builder.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    //Get the widgets from the layout and link them to the variables
    private void setValues() {
        refresh = findViewById(R.id.Button_DoctorPanelRefresh);
        spinner = findViewById(R.id.MDocSpinner);
        remove = findViewById(R.id.Button_DoctorPanelRemove);

        //TODO, it will contain details about the selected doctor (etc appointments count)
        doctorInfo = findViewById(R.id.txt_DoctorPanelInfo);

        doctorID = findViewById(R.id.txt_MDocID);
        doctorFN = findViewById(R.id.txt_MDocFN);
        doctorLN = findViewById(R.id.txt_MDocLN);
        doctorEmail = findViewById(R.id.txt_MDocEmail);
        doctorPass = findViewById(R.id.txt_MDocsPassword);
        doctorSpecs = findViewById(R.id.txt_MDocSpecialization);
        createDoc = findViewById(R.id.Button_MDocsCreate);
        goBack = findViewById(R.id.Button_MDocsGoToHome);

        //Holds the context during operations in front of the DB
        myContext = this;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{"Refresh DB"});

        spinner.setAdapter(adapter);
    }

    //Get the list of doctors from DB
    private void getDoctors() {
        doctorsList = new ArrayList<>();
//        userInfo = new ArrayList<>();
        //Keep an instance and a reference to the DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Doctors");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    doctorsList.add(value.getValue(NewDoctor.class));
//                    int token = Objects.requireNonNull(value.getValue()).toString().indexOf('=');
//                    userInfo.add(value.getValue().toString().substring(token + 1));
                }
                if (doctorsList.size() == 0)
                    setSpinner(new String[]{"No Doctors in DB"});
                else {
                    String[] items = new String[doctorsList.size()];
                    for (int i = 0; i < doctorsList.size(); i++) {
                        doctor = doctorsList.get(i);
                        items[i] = doctor.getUserFirstName() + " " + doctor.getUserLastName() + ", " + doctor.getUserSpec();
                    }
                    setSpinner(items);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(myContext, "התרחשה שגיאה, אנא נסה שנית", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Update the display spinner with each change
    private void setSpinner(String[] list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }

    //When adding a new doctor, make sure the values are valid
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

    //Removal of a doctor from the database
    private void remove(NewDoctor doctor) {
        reference.child(doctor.getUserID()).removeValue();
    }
}