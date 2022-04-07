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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;


public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    String[] classData = {};

    private LayoutInflater layoutInflater;

    AppAdapter(String[] _data){
        classData = _data;
    }

    private Context mContext;

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

        String className = classData[position];
        holder.className.setText(className);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test123", "onClick: clicked on: " + classData);
                Snackbar errorSnack = Snackbar.make(view, "Classes - " + classData[position], Snackbar.LENGTH_SHORT);
                errorSnack.show();
                // put info to send here
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
