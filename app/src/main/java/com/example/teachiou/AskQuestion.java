package com.example.teachiou;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// CITATION: Code from WishList project was used as starting point
public class AskQuestion extends AppCompatActivity {

    public static FirebaseHelper firebaseHelper = new FirebaseHelper();
    private String body, title, answer, imageID, classname, username, userImageID;
    private int time;
    private boolean isAnswered;
    private String c;
    private EditText bodyET, titleET, imageET;
    private static String uid;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayList<String> classes = new ArrayList<String>();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        bodyET = findViewById(R.id.bodyET);
        titleET = findViewById(R.id.titleET);

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, classes);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Class: " + classes, Toast.LENGTH_SHORT).show();
            }
        });


        Intent intent = getIntent();
        c = intent.getStringExtra("className");

        //set DROP DOWN to THIS INTENT VALUE

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
                                classes.add(value);
                            }
                            Log.i("AHHHHHHHHHHHHHHHHH", classes.toString());

                            Object[] objectClassesArray = classes.toArray();

                            String[] stringClassArray = Arrays.copyOf(objectClassesArray, objectClassesArray.length, String[].class);

                            Log.i("REERREERREERREER", (String) stringClassArray[0] + " and " + stringClassArray[1]);

                            //firestoreCallback.onCallback(myClasses);

                        }
                    }
                });
    }

    public void addData(View v) {
        body = bodyET.getText().toString();
        title = titleET.getText().toString();
        classname = autoCompleteTextView.getText().toString();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        db.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String value = documentSnapshot.getString("NAME");
                    userImageID = documentSnapshot.getString("IMAGEID");

                    Question q = new Question(body, title, imageID, value, userImageID);
                    firebaseHelper.addQuestion(q, classname);
                    bodyET.setText("");
                    titleET.setText("");

                }
            }
        });



        //insert firebaseHelper code to addData

        //PASS VALUE OF DROP DOWN INTO addQuestion INSTEAD OF THE INTENT DATA

    }

    public void back(View v){
        Intent intent = new Intent(AskQuestion.this, dashboard.class);
        startActivity(intent);
    }

    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void openImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();

            uploadImage();
        }
    }

    public void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + "." + getFileExtention(imageUri));
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageID = uri.toString();
                        Log.i("url string", imageID);
                        pd.dismiss();
                    }
                });
            }
        });
    }
}
