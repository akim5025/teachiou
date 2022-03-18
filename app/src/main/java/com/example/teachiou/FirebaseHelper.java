package com.example.teachiou;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    public final String TAG = "Denna";
    private static String uid = null;            // var will be updated for currently signed in user
    // Create the reference to FirebaseAuth and FirebaseFirestore that will allow us to access
    // the current user and their data anywhere through the variable of this class
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public FirebaseHelper() {
        // get a reference to the instance of the auth and fire store elements
        // these lines of code establish the connections to the auth and
        // database we are linked to based on the json file
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void addUserToFirestore(String name, String newUID) {
        // This will add a document with the uid of the current user to the collection called "users"
        // For this we will create a Hash app since there are only two fields - a name and the uid value

        // The docID of the document we are adding will be equal to the uid of the current user
        // Similar to "making a new folder for this user"
        Map<String, Object> user = new HashMap<>();

        // put data into my object using a key value pair where we label each item we put in the Map
        // the key "name" is the key that is used to label the data in firestore
        // the parameter value of name is passed in to be the value assigned to name in firestore
        user.put("name", name);

        // this will create a new document in the collection "users" and
        // assign it to a docID that is equal to new ID
        db.collection("users").document(newUID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, name + "'s user account added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "'Error adding user account", e);
                    }
                });
    }



}

