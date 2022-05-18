package com.example.teachiou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class dashboard extends AppCompatActivity {

    //Coding in Flow: "BottomNavigationView with Fragments - Android Studio Tutorial"
    //https://www.youtube.com/watch?v=tPV8xA7m-iw&t=185s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

        //https://www.youtube.com/watch?v=x6-_va1R788

    }

    private BottomNavigationView.OnNavigationItemSelectedListener  navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = new FragmentHome();

                    switch(item.getItemId()){
                        case R.id.nav_home:
                            Log.i("+++++++++++++++", "home");
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.nav_notification:
                            Log.i("+++++++++++++++", "not");
                            selectedFragment = new FragmentNotifications();
                            break;
                        case R.id.nav_profile:
                            Log.i("+++++++++++++++", "prof");
                            selectedFragment = new FragmentProfile();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new FragmentSettings();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };

    public void toQuestion(View v) {
        Intent intent = new Intent(this, AppAdapter.AskQuestion.class);
        startActivityForResult(intent, 1);
    }

    public static class Comment {
        private String time, answer, username, userImageID, docID;
        private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        boolean isTeacher;
        /**
            comment.put("answer", c.getAnswer());
            comment.put("username", c.getUsername());
            comment.put("userImageID", c.getUserImageID());
            comment.put("time", c.getTime());
         **/
        protected Comment(Parcel in) {
            answer = in.readString();
            docID = in.readString();
            time = in.readString();
            username = in.readString();
            userImageID = in.readString();
        }

        public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
            @Override
            public Comment createFromParcel(Parcel in) {
                return new Comment(in);
            }

            @Override
            public Comment[] newArray(int size) {
                return new Comment[size];
            }
        };
    /**
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(answer);
            parcel.writeString(docID);
            parcel.writeString(username);
            parcel.writeString(time);
            parcel.writeString(userImageID);
        }
    **/
        public Comment(String answer, String username, String userImageID) {
            this.username = username;
            this.answer = answer;
            this.docID = "";
            this.userImageID  = userImageID;
            this.time = String.valueOf(timestamp.getTime());
        }

        public Comment() {

        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserImageID() {
            return userImageID;
        }

        public void setUserImageID(String userImageID) {
            this.userImageID = userImageID;
        }

        public String getDocID() {
            return docID;
        }

        public void setDocID(String docID) {
            this.docID = docID;
        }



    }

    public static class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

        Context context;
        ArrayList<Comment> list;

        public CommentAdapter(Context context, ArrayList<Comment> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.commentitem, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Comment comment = list.get(position);
            holder.tvUsername.setText(comment.getUsername());
            holder.tvAnswer.setText(comment.getAnswer());
            Picasso.get().load(comment.getUserImageID()).resize(200, 200).into(holder.ivProfile);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvUsername, tvAnswer;
            ImageView ivProfile;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                tvUsername = itemView.findViewById(R.id.commenttvUsername);
                tvAnswer = itemView.findViewById(R.id.commenttvBody);
                ivProfile = itemView.findViewById(R.id.commentivProfile);
            }
        }

    }
}