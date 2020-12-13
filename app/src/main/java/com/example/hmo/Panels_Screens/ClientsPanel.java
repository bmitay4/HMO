package com.example.hmo.Panels_Screens;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hmo.Appointment_User.BookAppointment;
import com.example.hmo.General_Objects.NewDoctor;
import com.example.hmo.General_Objects.NewMember;
import com.example.hmo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientsPanel extends AppCompatActivity {
    private FirebaseDatabase fdb, database;
    private DatabaseReference ref, reference;
    private TextView fullInfo;
    private Button toHome, removeUser, editUser, refresh;
    private SearchView searchClients;
    private ListView clientList;
    private NewMember member;

    private ArrayAdapter usersAdapter;
    private ArrayList<NewMember> users;
    private ArrayList<String> usersInfo;
    private Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_client);

        //Get the widgets from the layout and link them to the variables
        setValues();

        //Get all customers from DB
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersInfo = new ArrayList<String>();
                users = new ArrayList<>();
                for (DataSnapshot members : snapshot.getChildren()) {
                    NewMember member = members.getValue(NewMember.class);
                    usersInfo.add(member.getUserFirstName()+" "+member.getUserLastName()+", "+member.getUserID());
                    users.add(member);
                }
                usersAdapter = new ArrayAdapter(myContext, android.R.layout.simple_list_item_1, usersInfo);
                clientList.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Highlight the selection from the list, and enter the customer details accordingly
        clientList.setOnItemClickListener((parent, view, position, id) -> {
            fullInfo.setText(users.get(position).toString());
            member = users.get(position);
        });

        //Handles the input obtained from the search model
        searchClients.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return true;
            }
        });

        //Refresh the DB (relevant in case a new client is added through another admin)
        refresh.setOnClickListener(v -> {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    usersInfo = new ArrayList<String>();
                    users = new ArrayList<>();
                    for (DataSnapshot members : snapshot.getChildren()) {
                        NewMember member = members.getValue(NewMember.class);
                        usersInfo.add(member.getUserFirstName()+" "+member.getUserLastName()+", "+member.getUserID());
                        users.add(member);
                    }
                    usersAdapter = new ArrayAdapter(myContext, android.R.layout.simple_list_item_1, usersInfo);
                    clientList.setAdapter(usersAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        });

        //Handles the removal of the requested client
        removeUser.setOnClickListener(v -> {
            if(member == null)
                new AlertDialog.Builder(this)
                        .setTitle("שגיאה")
                        .setMessage("לא נבחר לקוח מהרשימה").show();
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
                builder.setTitle("אזהרה");
                builder.setMessage("האם אתה בטוח שברצונך להסיר את לקוח "
                        + member.getUserFirstName() + " " + member.getUserLastName() +
                        " מהמאגר? אין אפשרות לבטל פעולה זו");

                builder.setPositiveButton("כן, מחק", (dialog, which) -> {
                    remove(member);
                    dialog.dismiss();
                });

                builder.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //Return home
        toHome.setOnClickListener(v->finish());

    }

    private void setValues() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
//        fdb = FirebaseDatabase.getInstance();
//        ref = fdb.getReference();
        fullInfo = findViewById(R.id.txt_MClientFullInfo);
        clientList = findViewById(R.id.MClientList);
        toHome = findViewById(R.id.button_MClientToHome);
        removeUser = findViewById(R.id.Button_ClientPanelRemove);

        //TODO, it will be possible to edit client details (e.g. name or DOB update)
        editUser = findViewById(R.id.Button_ClientPanelEdit);
        searchClients = findViewById(R.id.Search_ClientPanel);
        refresh = findViewById(R.id.Button_ClientPanelRefresh);

        searchClients.setQueryHint("חפש לקוח לפי שם או ת.ז.");
        //Holds the context during operations in front of the DB
        myContext = this;
    }

    //Removal of a doctor from the database
    private void remove(NewMember member) {
        reference.child(member.getUserID()).removeValue();
        Toast.makeText(this, "הלקוח נמחק בהצלחה", Toast.LENGTH_SHORT).show();
    }

    //Print function, for debug purpose
    private void print(Object s){
        System.out.println(s);
    }


//    private void getClients() {
//        userInfo = new ArrayList<>();
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot value : dataSnapshot.getChildren()) {
//                    NewMember member = (NewMember)value.getValue(NewMember.class);
//                    userInfo.add(member);
//                }
//                if (userInfo.size() == 0)
//                    setSpinner(new String[]{"No Clients in DB"});
//                else {
//                    String[] items = new String[userInfo.size()];
//                    for (int i = 0; i < userInfo.size(); i++) {
//                        NewMember member = userInfo.get(i);
//                        items[i] = member.getUserID()+ ", " +member.getUserFirstName() + " " + member.getUserLastName();
//                    }
//                    setSpinner(items);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
//
//    private void setSpinner(String[] list) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
//        spinner.setAdapter(adapter);
//    }
}