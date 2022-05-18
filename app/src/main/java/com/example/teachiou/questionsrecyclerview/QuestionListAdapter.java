package com.example.teachiou.questionsrecyclerview;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teachiou.FirebaseHelper;
import com.example.teachiou.R;
import com.example.teachiou.commentsrecyclerview.Comment;
import com.example.teachiou.commentsrecyclerview.CommentAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.myViewHolder>{

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Context context;
    ArrayList<Question> questionArrayList;
    ArrayList<Comment> commentArrayList;
    CommentAdapter commentAdapter;
    RecyclerView commentRecyclerView;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String uid = "";
    private String answer;
    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;
    private String userImageID;

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


        db = FirebaseFirestore.getInstance();
        Question question = questionArrayList.get(position);
        holder.tvTitle.setText(question.getTitle());
        holder.tvBody.setText(question.getBody());
        holder.tvUsername.setText(question.getUsername());
        Picasso.get().load(question.getImageID()).into(holder.ivQuestion);
        Picasso.get().load(question.getUserImageID()).resize(200, 200).into(holder.ivProfile);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        holder.buttonAnswer.setOnClickListener(new View.OnClickListener(){
                                                   @Override
                                                   public void onClick(View view) {
                                                       Log.i("BUTOOOOON", "this button works");
                                                       db = FirebaseFirestore.getInstance();
                                                       mAuth = FirebaseAuth.getInstance();
                                                       uid = mAuth.getUid();
                                                       db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                           @Override
                                                           public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                               if (documentSnapshot.exists()) {
                                                                   answer = holder.ETAnswer.getText().toString();
                                                                   String value = documentSnapshot.getString("NAME");
                                                                   String imgID = documentSnapshot.getString("IMAGEID");
                                                                   Comment q = new Comment(answer, value, imgID);
                                                                   firebaseHelper.addComment(q, question.getClassname(), question.getDocID());
                                                                   holder.ETAnswer.setText("");

                                                               }
                                                           }
                                                       });
                                                   }
                                               });

        holder.commentRecyclerView.setHasFixedSize(true);
        holder.commentRecyclerView.setLayoutManager(layoutManager);
        commentArrayList = new ArrayList<>();
        commentAdapter = new CommentAdapter(holder.commentRecyclerView.getContext(), commentArrayList);
        holder.commentRecyclerView.setAdapter(commentAdapter);
        EventChangeListener(question);
    }

    private void EventChangeListener(Question q) {

        Log.i("classname + id", q.getClassname() + ", " + q.getDocID());
        db.collection("classes").document(q.getClassname()).collection("questions").document(q.getDocID()).collection("comments").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                commentArrayList.add(dc.getDocument().toObject(Comment.class));
                            }
                        }
                        Log.i("comments array", commentArrayList.toString());
                        commentAdapter.notifyDataSetChanged();

                    }
                });
    }


    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView tvUsername, tvTitle, tvBody;
        ImageView ivProfile, ivQuestion;
        RecyclerView commentRecyclerView;
        EditText ETAnswer;
        Button buttonAnswer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ivQuestion = itemView.findViewById(R.id.ivQuestion);
            commentRecyclerView = itemView.findViewById(R.id.commentrecyclerview);
            ETAnswer = itemView.findViewById(R.id.commentAnswerET);
            buttonAnswer = itemView.findViewById(R.id.submitAnswerButton);


        }
    }
}
