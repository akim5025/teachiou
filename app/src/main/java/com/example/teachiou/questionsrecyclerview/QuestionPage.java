package com.example.teachiou.questionsrecyclerview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.teachiou.AskQuestion;
import com.example.teachiou.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuestionPage extends AppCompatActivity {
    // CITATION: Code from Wishlist project was used as starting point
    private ArrayList<Question> myList;
    private static final String TAG = "Denna";
    private String c;
    RecyclerView recyclerView;
    ArrayList<Question> questionArrayList;
    QuestionListAdapter questionListAdapter;
    FirebaseFirestore db;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        //myList.add(new Question());

        //myList = MainActivity.firebaseHelper.getWishListItems();
        Intent intent = getIntent();
        c = intent.getStringExtra("className");
        ArrayAdapter<Question> listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, myList);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        questionArrayList = new ArrayList<Question>();
        questionListAdapter = new QuestionListAdapter(QuestionPage.this, questionArrayList);

        recyclerView.setAdapter(questionListAdapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionArrayList.clear();
                EventChangeListener();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        EventChangeListener();

    }

    private void EventChangeListener() {
        Intent intent = getIntent();
        c = intent.getStringExtra("className");
        db.collection("classes").document(c).collection("questions").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                questionArrayList.add(dc.getDocument().toObject(Question.class));
                            }
                        }

                        questionListAdapter.notifyDataSetChanged();

                    }
                });

    }

    public void newQuestion(View v){
        Intent intent = new Intent(QuestionPage.this, AskQuestion.class);
        intent.putExtra("className", c);
        startActivityForResult(intent, 1);
    }


}
