package com.example.teachiou;

import static com.example.teachiou.MainActivity.firebaseHelper;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SignUpScreen extends AppCompatActivity {

    EditText emailET, passwordET, nameET;
    String name, userID, imageID;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    private Uri imageUri;
    private static final int IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        nameET = findViewById(R.id.editTextName);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void signUp(String email, String password) {
        Intent intent = new Intent(this, roleSelection.class);
        name = nameET.getText().toString();
        Map<String, String> mapHolder = new HashMap<String, String>();
        // If the email and password passed in are not null, then try to create a User
        if (email != null && password != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign up success, update UI with the signed-in user's information
                                Log.i("Denna", "createUserWithEmail:success");
                                Toast.makeText(SignUpScreen.this, "Authentication complete.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseHelper.getmAuth().getCurrentUser();
                                userID = user.getUid();
                                // add a document to fire store with the users name and their unique UID from auth account
                                firebaseHelper.addUserToFirestore(user.getUid());

                                Map<String, Object> dataStart = new HashMap<String, Object>();
                                dataStart.put("NAME", name);
                                dataStart.put("EMAIL", email);
                                dataStart.put("PASSWORD", password);
                                dataStart.put("ROLE", "");
                                dataStart.put("CLASSES", mapHolder);
                                dataStart.put("IMAGEID", imageID);

                                db.collection("users").document(userID).set(dataStart);

                                Log.i("/////////////////// CONSOLE", user.getUid());
                                // This is needed to help with asynchronous method calls in firebase
                                firebaseHelper.attachReadDataToUser();

                                startActivity(intent);

                            } else {
                                // If sign up fails, display a message to the user.
                                Log.i("Denna", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void handleAuthChange(View v) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        Log.i("Denna",  email + " " + password);

        switch (v.getId()) {
            case R.id.signUpButton:
                signUp(email, password);
                break;
        }
    }

    public void signInScreen(View v) {
        Intent intent = new Intent(this, MainActivity.class);
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
