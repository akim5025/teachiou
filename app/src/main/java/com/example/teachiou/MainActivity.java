package com.example.teachiou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    EditText emailET, passwordET;
    TextView authStatusTV; // might not be used. Textview that holds whether or not user is already signed in
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // Send to home page (add later)
        }
        // else {
        //     authStatusTV.setText("onStart reloaded and user is null");
        //     // Take any action needed here when screen loads and a user is NOT logged in
        // }
    }

    public void signIn(String email, String password) {

        // Intent intent = new Intent(this, FragmentHome.class); SEND TO HOME PAGE

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("Denna", "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Authentication complete.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            // startActivity(intent);  SEND TO HOME PAGE

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Denna", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signUp(View v) {
        // Make references to EditText in xml
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

        // Get user data
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        Log.i(TAG, email + " " + password);

        // verify all user data is entered
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
        }

        // verify password is at least 6 char long (otherwise firebase will deny)
        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 char long", Toast.LENGTH_SHORT).show();
        }
        else {
            // code to sign up user

            firebaseHelper.getmAuth().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //sign up worked, user created, so we want a reference to our user
                                FirebaseUser user = firebaseHelper.getmAuth().getCurrentUser();

                                //add a doc to firestore with user's name and unique UID from auth account
                                firebaseHelper.addUserToFirestore(email, user.getUid());

                                firebaseHelper.updateUid(user.getUid());

                                firebaseHelper.attachReadDataToUser();

                                updateIfLoggedIn();

                                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                                startActivity(intent);
                            }
                            else{
                                //sign up fails
                                Log.d(TAG, "Sign up failed");
                            }
                        }
                    });
        }

        updateIfLoggedIn();
    }


    public void handleAuthChange(View v) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        Log.i("Denna",  email + " " + password);

        switch (v.getId()) {
            case R.id.signInButton:
                signIn(email, password);
                break;
        }
    }

    public void signUpScreen(View v) {
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }

    public void test(View v) {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }

    public void signOut(String email, String password) {
        mAuth.signOut();
        authStatusTV.setText("Signed out");
    }

}