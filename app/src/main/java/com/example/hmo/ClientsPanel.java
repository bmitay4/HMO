package com.example.hmo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientsPanel extends AppCompatActivity {
    private FirebaseDatabase fdb;
    private DatabaseReference ref;
    private TextView fullInfo;
    private Button toHome;
    private ListView clientList;
    private ArrayAdapter usersAdapter;
    private ArrayList<NewMember> users;
    private ArrayList<String> usersInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_client);

        setValues();

        ref.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersInfo = new ArrayList<String>();
                users = new ArrayList<>();
                for (DataSnapshot members : snapshot.getChildren()) {
                    NewMember member = members.getValue(NewMember.class);
                    usersInfo.add(member.getUserFirstName()+" "+member.getUserLastName()+", "+member.getUserID());
                    users.add(member);
                }
                usersAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, usersInfo);
                clientList.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        clientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fullInfo.setText(users.get(position).toString());
            }
        });

        toHome.setOnClickListener(v->finish());

    }

    private void setValues() {
        fdb = FirebaseDatabase.getInstance();
        ref = fdb.getReference();
        fullInfo = findViewById(R.id.txt_MClientFullInfo);
        clientList = findViewById(R.id.MClientList);
        toHome = findViewById(R.id.button_MClientToHome);
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