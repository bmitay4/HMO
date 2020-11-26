package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookAppointment extends AppCompatActivity {

    private FirebaseDatabase fdb;
    private DatabaseReference refdb;
    private FirebaseAuth mAuth;
    private NewMember user;
    private static final String TAG = "MyActivity";
    private ListView doclist, aptlist;
    private CalendarView cal;
    private ArrayAdapter arrdoc, times;
    private ArrayList<String> listingdocs;
    private String date, docid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        user = (NewMember) getIntent().getSerializableExtra("user");
        fdb = FirebaseDatabase.getInstance();
        refdb = fdb.getReference();
        cal = findViewById(R.id.pickdate);
        doclist = findViewById(R.id.doclist);
        aptlist = findViewById(R.id.aptlist);

        // Get list of all the doctors
        refdb.child("Doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listingdocs = new ArrayList<String>();
                for (DataSnapshot doc : snapshot.getChildren()) {
                    NewDoctor d = doc.getValue(NewDoctor.class);
                    listingdocs.add(d.getUserFirstName()+" "+d.getUserLastName());
                }
                String[] a = new String[listingdocs.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = listingdocs.get(i);
                }
                arrdoc = new ArrayAdapter(BookAppointment.this, android.R.layout.simple_list_item_1, listingdocs);
                doclist.setAdapter(arrdoc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // TODO: Need to make on click listener for the list to pick doc
        docid = "doc";

        // pick date
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                if (docid != null) {
                    date = dayOfMonth + "." + (month + 1) + "." + year;
                    String d = date.replace(".", "");

                    // Pick the right branch Events -> DocID -> Date
                    DatabaseReference eventrf = refdb.child("Appointments").child(docid).child(d);
                    // get all the avilable appointments
                    eventrf.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Appointment> listapt = new ArrayList<Appointment>();

                            for (DataSnapshot value : snapshot.getChildren()) {
                                Appointment apt = value.getValue(Appointment.class);
                                if (apt.getAvilable()) {
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
                                    Appointment picked_apt = (Appointment) parent.getItemAtPosition(position);
                                    Intent i = new Intent(BookAppointment.this, ConfirmAppointment.class);
                                    i.putExtra("user", user);
                                    i.putExtra("picked_apt", picked_apt);
                                    startActivity(i);
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
    }
}




