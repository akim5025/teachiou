package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// CITATION: Code from WishList project was used as starting point
public class AskQuestion extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String body, title, answer, imageID, classname;
    private int time;
    private boolean isAnswered;
    private String c;
    private EditText bodyET, titleET, imageET;
    private static String uid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> classes = new ArrayList<String>();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, classes);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Class: " + classes, Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        c = intent.getStringExtra("className");

        //set DROP DOWN to THIS INTENT VALUE

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        db = FirebaseFirestore.getInstance();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, String> map = (HashMap) documentSnapshot.get("CLASSES");

                            for (Map.Entry mapElement : map.entrySet()) {
                                String value = (String) mapElement.getValue();
                                classes.add(value);
                            }
                            Log.i("AHHHHHHHHHHHHHHHHH", classes.toString());

                            Object[] objectClassesArray = classes.toArray();

                            String[] stringClassArray = Arrays.copyOf(objectClassesArray, objectClassesArray.length, String[].class);

                            Log.i("REERREERREERREER", (String) stringClassArray[0] + " and " + stringClassArray[1]);

                            //firestoreCallback.onCallback(myClasses);

                        }
                    }
                });
    }

    public void addData(View v) {
        body = bodyET.getText().toString();
        title = titleET.getText().toString();

        Question q = new Question(body, title);
        //insert firebaseHelper code to addData

        //PASS VALUE OF DROP DOWN INTO addQuestion INSTEAD OF THE INTENT DATA
        firebaseHelper.addQuestion(q, classname);
        bodyET.setText("");
        titleET.setText("");
    }

    public void back(View v){
        Intent intent = new Intent(AskQuestion.this, ActivityQuestionPage.class);
        startActivity(intent);
    }
}
