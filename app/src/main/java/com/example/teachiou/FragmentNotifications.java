package com.example.teachiou;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNotifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button sendNotification;

    public FragmentNotifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotifications.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotifications newInstance(String param1, String param2) {
        FragmentNotifications fragment = new FragmentNotifications();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        sendNotification = root.findViewById(R.id.testNotifButton);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        "/topics/all",
                        "Class Name",
                        "has been updated",
                        activity.getApplicationContext(),
                        activity);
                notificationsSender.SendNotifications();
                Log.d("ISHAAAAAAAAAAAAAAAAAAN", "Notification Sent");
                //https://www.youtube.com/watch?v=cyG5SAaucHs&t=16s
            }
        });
        return root;
    }
}