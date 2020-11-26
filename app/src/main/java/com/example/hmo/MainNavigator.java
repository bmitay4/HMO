package com.example.hmo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavigator extends AppCompatActivity {
    public static String uid,userID, userFirstName, userLastName, userEmail;
    private NewMember user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        user = (NewMember) getIntent().getSerializableExtra("user");

//        userID = getIntent().getStringExtra("userID");
//        userFirstName = getIntent().getStringExtra("userFirstName");
//        userLastName = getIntent().getStringExtra("userLastName");
//        userEmail = getIntent().getStringExtra("userEmail");
//        uid = getIntent().getStringExtra("uid");


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_medical, R.id.navigation_search, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void BookAppointment(){
        Intent intent = new Intent(getApplicationContext(), MainNavigator.class);
        intent.putExtra("user",user);
        startActivity(intent);

    }

}