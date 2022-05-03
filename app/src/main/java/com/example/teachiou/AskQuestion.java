package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

// CITATION: Code from WishList project was used as starting point
public class AskQuestion extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String body, title, answer, imageID, classname;
    private int time;
    private boolean isAnswered;
    private String c;
    private EditText bodyET, titleET, imageET, classET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        bodyET = findViewById(R.id.bodyET);
        titleET = findViewById(R.id.titleET);
        classET = findViewById(R.id.classET);

        Intent intent = getIntent();
        c = intent.getStringExtra("className");

        //set DROP DOWN to THIS INTENT VALUE
    }

    public void addData(View v) {
        body = bodyET.getText().toString();
        title = titleET.getText().toString();
        classname = classET.getText().toString();

        Question q = new Question(body, title);
        //insert firebaseHelper code to addData

        //PASS VALUE OF DROP DOWN INTO addQuestion INSTEAD OF THE INTENT DATA
        firebaseHelper.addQuestion(q, classname);
        bodyET.setText("");
        titleET.setText("");
        classET.setText("");
    }

    public void back(View v){
        Intent intent = new Intent(AskQuestion.this, ActivityQuestionPage.class);
        startActivity(intent);
    }
}