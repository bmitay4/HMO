package com.example.hmo.Appointment_Doctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DoctorAppointment extends AppCompatActivity {
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private NewDoctor doctor;
    private TextView docName, docInfo;
    private ArrayList<Appointment> DocAppointmentArray;
    private ArrayList<String> DocAppointmentStrArray;
    private ListView DocAppointment;
    private ArrayAdapter DocAppointmentAdapter;
    private Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);

        setValues();
        goHome.setOnClickListener(v->finish());

        refdb.child("DoctorAppointments").child(doctor.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DocAppointmentArray = new ArrayList<Appointment>();
                DocAppointmentStrArray = new ArrayList<String>();
                for (DataSnapshot app_date : snapshot.getChildren()) {
                    for (DataSnapshot app_info : app_date.getChildren()) {
                        Appointment docApp = app_info.getValue(Appointment.class);
                        DocAppointmentArray.add(docApp);
                        DocAppointmentStrArray.add(docApp.getDate() + ", " + docApp.getTime() + ", " + docApp.getUserName()+" "+docApp.getUserLastName());

                    }
                }
                DocAppointmentAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, DocAppointmentStrArray);
                DocAppointment.setAdapter(DocAppointmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DocAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                docInfo.setText(DocAppointmentArray.get(position).toString());
            }
        });
    }

    private void setValues(){
        doctor = (NewDoctor) getIntent().getSerializableExtra("doctor");
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        docName = findViewById(R.id.txt_DocAppName);
        docName.setText(" ד\"ר " +doctor.getUserFirstName()+" "+doctor.getUserLastName() + " שלום, ");
        docInfo = findViewById(R.id.txt_DoctorAppointmentInfo);
        DocAppointment = findViewById(R.id.list_DoctorAppointmens);
        goHome = findViewById(R.id.Button_DocAppointmentGoHome);
    }
}