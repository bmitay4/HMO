package com.example.hmo.Appointment_User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAppointments extends AppCompatActivity {
    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private NewMember user;
    private ArrayList<Appointment> userAppointmentArray;
    private ArrayList<String> userAppointmentStrArray;
    private ListView userAppointment;
    private ArrayAdapter userAppointmentAdapter;
    private TextView userInfo;
    private Button goHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);

        setValues();

        goHome.setOnClickListener(v->finish());

        refdb.child("UserAppointments").child(user.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAppointmentArray = new ArrayList<Appointment>();
                userAppointmentStrArray = new ArrayList<String>();
                for (DataSnapshot app_date : snapshot.getChildren()) {
                    for (DataSnapshot app_info : app_date.getChildren()) {
                        Appointment userApp = app_info.getValue(Appointment.class);
                        userAppointmentArray.add(userApp);
                        userAppointmentStrArray.add(userApp.getDate() + ", " + userApp.getTime() + ", ד\"ר " + userApp.getDocName());

                    }
                }
                userAppointmentAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, userAppointmentStrArray);
                userAppointment.setAdapter(userAppointmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        userAppointment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userInfo.setText(userAppointmentArray.get(position).toString(user));
            }
        });

    }


    private void setValues() {
        user = (NewMember) getIntent().getSerializableExtra("member");
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        userInfo = findViewById(R.id.txt_userAppointmentInfo2);
        userAppointment = findViewById(R.id.list_UserAppointmens);
        userInfo.setText("");
        goHome = findViewById(R.id.button_userAppointmentGoHome2);
    }
}