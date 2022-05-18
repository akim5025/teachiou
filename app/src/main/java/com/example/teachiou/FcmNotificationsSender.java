package com.example.teachiou;
// CODE TAKEN FROM: https://www.youtube.com/watch?v=fue1-y9pLCY

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender {

    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;


    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AAAAkI0p0C4:APA91bFHrBTm0Tvfv3uoRUeSOQLqg7opae0IgYiO_ZsEDvHdB2hD_BFMRrp1VMRwIwoFPJ_KmErrjKTt333_zfxXNYU6q6LHpmBWgTdvclIe0ODv5Wo7YWHXeCAXC71yVig-PEdEFdxe";

    public FcmNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;


    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.icon); // enter icon that exists in drawable only


            mainObj.put("notification", notiObject);


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static class QuestionPage extends AppCompatActivity {
        // CITATION: Code from Wishlist project was used as starting point
        private ArrayList<classListItem.Question> myList;
        private static final String TAG = "Denna";
        private String c;
        RecyclerView recyclerView;
        ArrayList<classListItem.Question> questionArrayList;
        ClassSelection.QuestionListAdapter questionListAdapter;
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
            ArrayAdapter<classListItem.Question> listAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, myList);

            recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            db = FirebaseFirestore.getInstance();
            questionArrayList = new ArrayList<classListItem.Question>();
            questionListAdapter = new ClassSelection.QuestionListAdapter(QuestionPage.this, questionArrayList);

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
                                    questionArrayList.add(dc.getDocument().toObject(classListItem.Question.class));
                                }
                            }

                            questionListAdapter.notifyDataSetChanged();

                        }
                    });

        }

        public void newQuestion(View v){
            Intent intent = new Intent(QuestionPage.this, AppAdapter.AskQuestion.class);
            intent.putExtra("className", c);
            startActivityForResult(intent, 1);
        }


    }
}