package com.example.teachiou;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * The purpose of this class is to hold ALL the code to communicate with Firebase.  This class
 * will connect with Firebase auth and Firebase firestore.  Each class that needs to verify
 * authentication OR access data from the database will reference a variable of this class and
 * call a method of this class to handle the task.  Essentially this class is like a "gopher" that
 * will go and do whatever the other classes want or need it to do.  This allows us to keep all
 * our other classes clean of the firebase code and also avoid having to update firebase code
 * in many places.  This is MUCH more efficient and less error prone.
 */
public class FirebaseHelper {
    public final String TAG = "Denna";
    private static String uid = null;            // var will be updated for currently signed in user
    // inside MainActivity with the mAuth var

    //Create reference to firebase auth and firestore that will allow us to ask the current user their data anywhere through a variable of this class

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<classListItem> myItems = new ArrayList<>();

    public FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        attachReadDataToUser();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void attachReadDataToUser() {
        //this will help avoid async method calls
        //first verify user, if there is one, set them up to read data.
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getUid();
            //more here
            readData(new FirestoreCallback() {
                @Override
                public void onCallback(ArrayList<classListItem> myList) {
                    Log.i(TAG, "Inside attachReadDataToUser, onCallBack" + myList.toString());
                }
            });
        }
    }

    public void addUserToFirestore(String newUID) {
        //This will add a document with the UID of the current user to the collection called "users"
        //For this we will create a hashmap since here are only two fields - a name and the UID value.

        //the doc id of the document we are adding is equal to the uid of the user
        //similar to how I said "we are making a new folder for this user"
        Map<String, Object> user = new HashMap<>();
        //put data into object using key value pair where I label each item I put in the Map
        //the key name is the key that is used to label the data in firestore.
        //the parameter of name is passed in to be the value assigned to name in firestore.

        //this will create a new document in the collection "users" and assign it a docID that is equal to new ID
        db.collection("users").document(newUID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "user account added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding user account", e);
                    }
                });

    }

    public void addData(classListItem wish) {
        //add wishlist item to the database
        //this method will be overloaded nd the other method will incorporate the interface to
        //handle asynch calls for reading data to keep myItems AL up to date.
        addData(wish, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<classListItem> myList) {
                Log.i(TAG, "Indide addData, finished:  " + myList.toString());
            }
        });
    }

    //this method will do the actual work of adding the wishlist item to the database.
    private void addData(classListItem classItem, FirestoreCallback firestoreCallback) {
        db.collection("users").document(uid).collection("myWishList")
                .add(classItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // we are going to update the document we just added by
                        // editing the docID instance variable so that it knows what
                        // the value is for its docID in firestore.

                        // in the onSuccess method, the documentReference parameter
                        // contains a reference to the newly created document. We
                        // can use this to extract the docID from firestore.

                        db.collection("users").document(uid).collection("myWishList")
                                .document(documentReference.getId())
                                .update("docID", documentReference.getId());
                        Log.i(TAG, "just added " + classItem.getClassName());
                        readData(firestoreCallback);

                        // If we want the arrayList to be updated NOW, we call
                        // readData.  If we don't care about continuing our work, then
                        // you don't need to call readData

                        // later on, experiment with commenting this line out, see how it is different.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding element", e);
                    }
                });
    }

    //https://firebase.google.com/docs/firestore/manage-data/add-data#java_16
// Add document
    public void addRole(String role, String uid) {
        DocumentReference userRef = db.collection("user").document(uid);
        Map<String, Object> user = new HashMap<>();
        user.put("roleStatus", role);
        userRef
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


    public ArrayList<classListItem> getWishListItems() {
        return myItems;
    }


    public void editData(classListItem w) {
        // edit WishListItem w to the database
        // this method is overloaded and incorporates the interface to handle the asynch calls
        editData(w, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<classListItem> myList) {
                Log.i(TAG, "Inside editData, onCallback " + myList.toString());
            }
        });
    }

    private void editData(classListItem w, FirestoreCallback firestoreCallback) {
        String docId = w.getDocID();
        db.collection("users").document(uid).collection("myWishList")
                .document(docId)
                .set(w)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "Success updating document");
                        readData(firestoreCallback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error updating document", e);
                    }
                });
    }

    public void deleteData(classListItem w) {
        // delete item w from database
        // this method is overloaded and incorporates the interface to handle the asynch calls
        deleteData(w, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<classListItem> myList) {
                Log.i(TAG, "Inside deleteData, onCallBack" + myList.toString());
            }
        });

    }

    public void deleteData(classListItem w, FirestoreCallback firestoreCallback) {
        // delete item w from database
        String docId = w.getDocID();
        db.collection("users").document(uid).collection("myWishList")
                .document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, w.getClassName() + " successfully deleted");
                        readData(firestoreCallback);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error deleting document", e);
                    }
                });
    }

    public void updateUid(String uid) {

    }

    /* https://www.youtube.com/watch?v=0ofkvm97i0s
    This video is good!!!   Basically he talks about what it means for tasks to be asychronous
    and how you can create an interface and then using that interface pass an object of the interface
    type from a callback method and access it after the callback method.  It also allows you to delay
    certain things from occuring until after the onSuccess is finished.
     */

    private void readData(FirestoreCallback firestoreCallback) {
        myItems.clear();     //clears out the array list so that we can put in new values
        db.collection("users").document(uid).collection("myWishList").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            // loop through all elements in the snapshot and convert them into wishList items
                            // and then add them to my arraylist
                            for (DocumentSnapshot doc: task.getResult()) {
                                // processing every resulting document from the query we made.
                                // this is a snapshot of the data at this moment in time, when we requested for the data.

                                //converts firestore item into java object

                                classListItem w = doc.toObject(classListItem.class);
                                myItems.add(w);
                            }
                            //still inside the oncomplete, i want to call my onCallback method so that the interface
                            //can do its joba nd let whoever is wating know that the async method is done.
                            Log.i(TAG, "Success reading data: " + myItems.toString());
                            firestoreCallback.onCallback(myItems);
                        }
                    }
                });
    }

    //https://stackoverflow.com/questions/48499310/how-to-return-a-documentsnapshot-as-a-result-of-a-method/48500679#48500679
    public interface FirestoreCallback {
        void onCallback(ArrayList<classListItem> myList);

    }
}

