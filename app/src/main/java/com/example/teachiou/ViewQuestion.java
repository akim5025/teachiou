package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

// CITATION: Code from Wishlist project was used as starting point.
public class ViewQuestion extends AppCompatActivity {

    private EditText answerET;
    private Question q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        answerET = findViewById(R.id.answerET);

        Intent intent = getIntent();
        q = intent.getParcelableExtra("QUESTION");
        answerET.setText(q.getAnswer());
    }



}