package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.widget.EditText;
import android.widget.TextView;

// CITATION: Code from WishList project was used as starting point
public class ViewQuestion extends AppCompatActivity {

    private EditText answerET;
    private TextView bodyTV;
    private Question q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        answerET = findViewById(R.id.answerET);
        bodyTV = findViewById(R.id.bodyTV);

        Intent intent = getIntent();
        q = intent.getParcelableExtra("QUESTION");
        answerET.setText(q.getAnswer());
        bodyTV.setText(q.getBody());

        // CITATION: making ETs uneditable -->
        // https://stackoverflow.com/questions/6555455/how-to-set-editable-true-false-edittext-in-android-programmatically
        KeyListener variable;
        variable = answerET.getKeyListener();
        answerET.setKeyListener(null);
        // when teacher edits/answers isAnswered is set to true
        if (isTeacher){
            answerET.setKeyListener(variable);
        }
    }
}