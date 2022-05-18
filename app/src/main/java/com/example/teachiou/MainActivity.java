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

    // create public static FirebaseHelper variable
    // this will allow ALL the other activities to access
    // this variable by referring to it as MainActivity.firebaseHelper
    public static FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate FirebaseHelper var
        firebaseHelper = new FirebaseHelper();

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

        Intent intent = new Intent(this, dashboard.class); //SEND TO HOME PAGE

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

                            firebaseHelper.attachReadDataToUser();


                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("Denna", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);

    }

    public void signUpScreen(View v) {
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }


}