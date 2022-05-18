package com.example.teachiou;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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

                                    Intent intent = new Intent(activity, FcmNotificationsSender.QuestionPage.class);
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

    // CITATION: Code from WishList project was used as starting point
    public static class AskQuestion extends AppCompatActivity {

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
                        classListItem.Question q = new classListItem.Question(body, title, imageID, value, userImageID, classname);
                        firebaseHelper.addQuestion(q, classname);
                        Log.i("CLASSNAME", classname);
                        bodyET.setText("");
                        titleET.setText("");
                    }
                }
            });

            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                    "/topics/" + classname.replace(" ", ""),
                    classname,
                    "A question has been added to " + classname,
                    getApplicationContext(),
                    AskQuestion.this);
            notificationsSender.SendNotifications();


            //insert firebaseHelper code to addData

            //PASS VALUE OF DROP DOWN INTO addQuestion INSTEAD OF THE INTENT DATA

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

        public void back(View v){
            Intent intent = new Intent(AskQuestion.this, dashboard.class);
            startActivity(intent);
        }
    }
}
