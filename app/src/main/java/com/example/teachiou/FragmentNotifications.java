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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    private ArrayList<String> classList = new ArrayList<String>();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button sendNotification;

    private static String uid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseHelper firebaseHelper;


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

        firebaseHelper = new FirebaseHelper();
        firebaseHelper.attachReadDataToUser();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, String> map = (HashMap) documentSnapshot.get("CLASSES");

                            for (Map.Entry mapElement : map.entrySet()) {
                                String value = (String) mapElement.getValue();
                                classList.add(value);
                            }
                            Log.i("AHHHHHHHHHHHHHHHHH", classList.toString());

                            Object[] objectClassesArray = classList.toArray();

                            String[] stringClassArray = Arrays.copyOf(objectClassesArray, objectClassesArray.length, String[].class);


                            LinearLayout switchLayout = (LinearLayout) root.findViewById(R.id.switchNotif);



                            LinearLayout.LayoutParams params =
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.FILL_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(30, 20, 30, 20);

                            for (int i = 0; i < classList.size(); i++) {

                                LayoutInflater inflater = getLayoutInflater();

                                Switch swtTag = new Switch ((AppCompatActivity)root.getContext());

                                for (int j = 0; j < 4; j++) {
                                    swtTag.setLayoutParams(params);
                                    swtTag.setWidth(90);
                                    swtTag.setTag("switch"+i);
                                    swtTag.setHeight(80);
                                    swtTag.setGravity(11);
                                    swtTag.setChecked(true);
                                    swtTag.setText(classList.get(i));
                                    swtTag.setTextSize(20);
                                }

                                switchLayout.addView(swtTag);


                        }
                    }
                }
                });

        sendNotification = root.findViewById(R.id.updateSettings);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup layout = (ViewGroup) root.findViewById(R.id.switchNotif);

                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    if (child instanceof Switch) {
                        Switch curSwitch = (Switch) child;
                        if (curSwitch.isChecked()) {
                            String switchTopic = (String) curSwitch.getText();
                            switchTopic = switchTopic.replace(" ", "");
                            FirebaseMessaging.getInstance().subscribeToTopic(switchTopic);
                            Log.i("PPPPPPPPPPP",switchTopic);
                        }
                        else {
                            String switchTopic = (String) curSwitch.getText();
                            switchTopic = switchTopic.replace(" ", "");
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(switchTopic);
                            Log.i("REMOVED!!!!!!!!!!!!!!", switchTopic);

                        }
                    }
                }
                Toast toast = Toast.makeText((AppCompatActivity)v.getContext(), "Notification Settings Have Been Updated. Settings May Take a Minute to Apply", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return root;

    }



}