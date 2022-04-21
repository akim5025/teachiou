package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

// CITATION: Code from WishList project was used as starting point
public class AskQuestion extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String body, title, answer, imageID;
    private int time;
    private boolean isAnswered;

    private EditText bodyET, titleET, imageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        bodyET = findViewById(R.id.bodyET);
        titleET = findViewById(R.id.titleET);
    }

    public void addData(View v) {
        body = bodyET.getText().toString();
        title = titleET.getText().toString();

        Question q = new Question(body, title);
        //insert firebaseHelper code to addData
        firebaseHelper.addQuestion(q);
        bodyET.setText("");
        titleET.setText("");
    }

    public void back(View v){
        Intent intent = new Intent(AskQuestion.this, ActivityQuestionPage.class);
        startActivity(intent);
    }
}