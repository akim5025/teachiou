package com.example.teachiou;


import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.myViewHolder>{

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Context context;
    ArrayList<Question> questionArrayList;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String uid = "";

    public QuestionListAdapter(Context context, ArrayList<Question> questionArrayList) {
        this.context = context;
        this.questionArrayList = questionArrayList;
    }

    @NonNull
    @Override
    public QuestionListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.questionitem, parent, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListAdapter.myViewHolder holder, int position) {



        Question question = questionArrayList.get(position);
        holder.tvTitle.setText(question.getTitle());
        holder.tvBody.setText(question.getBody());
        holder.tvUsername.setText(question.getUsername());
        Picasso.get().load(question.getImageID()).into(holder.ivQuestion);
        Picasso.get().load(question.getUserImageID()).resize(200, 200).into(holder.ivProfile);



    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername, tvTitle, tvBody;
        ImageView ivProfile, ivQuestion;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ivQuestion = itemView.findViewById(R.id.ivQuestion);

        }
    }

}
