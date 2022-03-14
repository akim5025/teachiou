package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void test(View v) {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

    public void testSignUp(View v) {
        Intent intent = new Intent(this, roleSelection.class);
        startActivity(intent);
    }

}