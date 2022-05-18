package com.example.teachiou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class dashboard extends AppCompatActivity {

    //Coding in Flow: "BottomNavigationView with Fragments - Android Studio Tutorial"
    //https://www.youtube.com/watch?v=tPV8xA7m-iw&t=185s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

        //https://www.youtube.com/watch?v=x6-_va1R788

    }

    private BottomNavigationView.OnNavigationItemSelectedListener  navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new FragmentHome();

                    switch(item.getItemId()){
                        case R.id.nav_home:
                            Log.i("+++++++++++++++", "home");
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.nav_notification:
                            Log.i("+++++++++++++++", "not");
                            selectedFragment = new FragmentNotifications();
                            break;
                        case R.id.nav_profile:
                            Log.i("+++++++++++++++", "prof");
                            selectedFragment = new FragmentProfile();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new FragmentSettings();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    public void toQuestion(View v) {
        Intent intent = new Intent(this, AskQuestion.class);
        startActivityForResult(intent, 1);
    }
}