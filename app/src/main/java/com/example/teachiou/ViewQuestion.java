package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.KeyListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


// CITATION: Code from WishList project was used as starting point
public class ViewQuestion extends AppCompatActivity {

    private EditText answerET;
    private TextView bodyTV;
    private Question q;
    private boolean isTeacher;
    private String c;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static String uid;
    private String role = "";
    private Button buttonSave;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);

        db = FirebaseFirestore.getInstance();

        answerET = findViewById(R.id.answerET);
        bodyTV = findViewById(R.id.bodyTV);

        Intent intent = getIntent();
        q = intent.getParcelableExtra("QUESTION");
        answerET.setText(q.getAnswer());

        bodyTV.setText(q.getBody());

        c = intent.getStringExtra("className");
        // https://firebase.google.com/docs/firestore/query-data/get-data
        // https://stackoverflow.com/questions/48492993/firestore-get-documentsnapshots-fields-value
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String role = (String) documentSnapshot.get("ROLE");
                        }
                    }
                });

        // CITATION: making ETs uneditable -->
        // https://stackoverflow.com/questions/6555455/how-to-set-editable-true-false-edittext-in-android-programmatically
        KeyListener variable;
        variable = answerET.getKeyListener();
        answerET.setKeyListener(null);
        // when teacher edits/answers isAnswered is set to true
        // access role. if role is set to teacher isTeacher is true

        if (role.equals("teacher")){
            isTeacher = true;
        }
        if (isTeacher){
            answerET.setKeyListener(variable);
        }

        buttonSave = findViewById(R.id.button_save);
        answerET.addTextChangedListener(answerTextWatcher);
    }

    //https://codinginflow.com/tutorials/android/textwatcher-enable-disable-button
    private TextWatcher answerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String answerInput = answerET.getText().toString().trim();
            buttonSave.setEnabled(!answerInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}