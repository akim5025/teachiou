package com.example.teachiou;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {


    // https://www.youtube.com/watch?v=5A_TqyzjByk
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // Later on, Get users classes from firestore instead of using this static list
    private String[] classes = {"Math", "English", "Java"};
    private View listItemsView;
    public String choseClass = "";
    FirebaseHelper firebaseHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private static String uid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> myClasses = new ArrayList<String>();



    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

        // this inflates the fragment_mains to the view group
        // the next line uses the listItemView to get the recycler view
        listItemsView = inflater.inflate(R.layout.fragment_home,container,false);
        recyclerView = listItemsView.findViewById(R.id.classesList);
        firebaseHelper = new FirebaseHelper();
        firebaseHelper.attachReadDataToUser();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // this actually loads the list items to the recycler view



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
                                myClasses.add(value);
                            }
                            Log.i("AHHHHHHHHHHHHHHHHH", myClasses.toString());

                            Object[] objectClassesArray = myClasses.toArray();

                            String[] stringClassArray = Arrays.copyOf(objectClassesArray, objectClassesArray.length, String[].class);

                            Log.i("REERREERREERREER", (String) stringClassArray[0] + " and " + stringClassArray[1]);

                            //firestoreCallback.onCallback(myClasses);

                            adapter = new AppAdapter(new FragmentHome(), stringClassArray);
                            recyclerView.setAdapter(adapter);

                        }
                    }
                });

        Log.i("8888888888888888888", firebaseHelper.getClassItems().toString());
        //firebaseHelper.getClassItems().toString()


        // Inflate the layout for this fragment
        return listItemsView;
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

}