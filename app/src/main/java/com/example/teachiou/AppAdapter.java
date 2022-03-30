package com.example.teachiou;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder>{

    String[] classData = {};

    private LayoutInflater layoutInflater;

    AppAdapter(String[] _data){
        classData = _data;
    }


    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = layoutInflater.from(parent.getContext());

        // this view is passed to the view holder and getItemCount item is used to
        // create the uis for the given array
        View view =layoutInflater.inflate(R.layout.list_class,parent,false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        String className = classData[position];
        holder.className.setText(className);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class AppViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIcon;
        TextView className;
            public AppViewHolder(@NonNull View itemView){
                super(itemView);
                imgIcon = itemView.findViewById(R.id.imgIcon);
                className = itemView.findViewById(R.id.className);
            }

    }
}
