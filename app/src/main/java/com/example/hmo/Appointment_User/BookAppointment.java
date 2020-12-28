package com.example.hmo.Appointment_User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.General_Objects.Appointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {

    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private NewMember user;
    private ListView doclist, aptlist;
    private CalendarView cal;
    private ArrayAdapter arrdoc, times;
    private Appointment picked_apt;
    private ArrayList<String> doc_full_name;
    private ArrayList<NewDoctor> docList;
    private String date, docid;
    private Button confirmButton, goBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        setValues();
        goBackButton.setOnClickListener(v -> finish());
        confirmButton.setOnClickListener(v -> confirmQueue());

        // Get list of all the doctors
        refdb.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doc_full_name = new ArrayList<String>();
                docList = new ArrayList<>();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    NewDoctor d = doc.getValue(NewDoctor.class);
                    assert d != null; //Should'nt be NULL at any case
                    doc_full_name.add(d.getUserFirstName() + " " + d.getUserLastName() + ", " + d.getUserSpec());
                    docList.add(d);
                }

                arrdoc = new ArrayAdapter(BookAppointment.this, android.R.layout.simple_list_item_1, doc_full_name);
                doclist.setAdapter(arrdoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Hold Doctor ID after the user picks it
        doclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                docid = docList.get(position).getUserID();
                if(date != null){
                    show_avilable_time();
                }
            }
        });



        // pick date
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                if (docid != null) {

                    String int_day_tostring = "" + dayOfMonth;
                    String int_month_tostring = ""+ (month+1);

                    if (int_day_tostring.length() < 2) {
                        date = "0"+dayOfMonth + ".";

                    }else{
                        date = dayOfMonth + ".";
                    }
                    if (int_month_tostring.length()<2){
                        date = date +"0"+(month+1)+"."+year;
                    }
                    else{
                        date = date +(month+1)+"."+year;
                        show_avilable_time();
                    }

                }
            }
        });
    }

    private void show_avilable_time() {
        String date2db = date.replace(".", "");
        // Pick the right branch Events -> DocID -> Date
        DatabaseReference ref_apt = refdb.child("Appointments").child(docid).child(date2db);

        // Gets all the available appointments
        ref_apt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Appointment> listapt = new ArrayList<Appointment>();
                for (DataSnapshot value : snapshot.getChildren()) {
                    Appointment apt = value.getValue(Appointment.class);
                    if (apt.getAvailable()) {
                        listapt.add(apt);
                    }
                }

                //Set as list and show on listview
                String[] a = new String[listapt.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = listapt.get(i).getTime();
                }
                times = new ArrayAdapter(BookAppointment.this, android.R.layout.simple_list_item_1, a);
                aptlist.setAdapter(times);

                //If clicked on one of the appointments
                aptlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        picked_apt = (Appointment) listapt.get(position);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setValues() {
        user = (NewMember) getIntent().getSerializableExtra("user");
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        cal = findViewById(R.id.pickdate);
        doclist = findViewById(R.id.doclist);
        doclist.setSelector(R.color.HMO_blue);
        aptlist = findViewById(R.id.aptlist);
        aptlist.setSelector(R.color.HMO_blue);
        confirmButton = findViewById(R.id.Button_AppointmentConfirm);
        goBackButton = findViewById(R.id.Button_AppointmentGoHome);

    }

    private void confirmQueue() {
        if (docid != null) {
            Intent i = new Intent(this, ConfirmAppointment.class);
            i.putExtra("user", user);
            i.putExtra("picked_apt", picked_apt);
            startActivity(i);
        } else
            Toast.makeText(this, "לא נבחר רופא או מועד לתור", Toast.LENGTH_LONG).show();

    }
}




