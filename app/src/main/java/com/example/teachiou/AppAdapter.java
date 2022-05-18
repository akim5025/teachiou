package com.example.teachiou;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    String[] classData = {};
    LinearLayout parentLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Context mContext;
    FragmentHome thisFrag;
    private LayoutInflater layoutInflater;

    public AppAdapter(FragmentHome fragmentHome, String[] _data){

        classData = _data;
        thisFrag = fragmentHome;
    }



    // https://github.com/mitchtabian/Recyclerview




    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = layoutInflater.from(parent.getContext());

        // this view is passed to the view holder and getItemCount item is used to
        // create the uis for the given array
        View view = layoutInflater.inflate(R.layout.list_class,parent,false);
        return new AppViewHolder(view);
    }



    // https://dev.to/theplebdev/adding-onclicklistener-to-recyclerview-in-android-3amb
    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String className = classData[position];
        db.collection("classes").document(className).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String imageID = (String) documentSnapshot.get("iconID");

                            holder.className.setText(className);
                            Picasso.get().load(imageID).resize(200, 200).into(holder.imgIcon);
                            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Log.d("test123", "onClick: clicked on: " + classData);

                                    // Snackbar errorSnack = Snackbar.make(view, "Classes - " + classData[position], Snackbar.LENGTH_SHORT);
                                    // errorSnack.show();

                                    AppCompatActivity activity = (AppCompatActivity)view.getContext();

                                    Intent intent = new Intent(activity, QuestionPage.class);
                                    intent.putExtra("className", classData[position]);
                                    activity.startActivity(intent);

                                    // put info to send here
                                }
                            });
                        }
                    }
                });


    }

    @Override
    public int getItemCount() {
        return classData.length;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView className;
        LinearLayout parentLayout;

            public AppViewHolder(@NonNull View itemView){
                super(itemView);
                imgIcon = itemView.findViewById(R.id.imgIcon);
                className = itemView.findViewById(R.id.className);
                parentLayout = itemView.findViewById(R.id.parent_layout);
         }
    }
}
