package com.example.teachiou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
// CITATION: Code from WishList project was used as starting point
public class ActivityQuestionPage extends AppCompatActivity {
    // CITATION: Code from Wishlist project was used as starting point
    private ArrayList<Question> myList;
    private static final String TAG = "Denna";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        //myList = MainActivity.firebaseHelper.getWishListItems();
        Intent intent = getIntent();
        String id = intent.getStringExtra("className");
        Log.d("test123", "onClick: clicked on: " + id);



        ArrayAdapter<Question> listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, myList);

        ListView listView = (ListView) findViewById(R.id.questionList);

        // listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ActivityQuestionPage.this, ViewQuestion.class);

                intent.putExtra("QUESTION", myList.get(i));
                startActivity(intent);

            }
        });
    }

    public void newQuestion(View v){
        Intent intent = new Intent(ActivityQuestionPage.this, AskQuestion.class);
        startActivityForResult(intent, 1);
    }


}
