package com.example.teachiou;

import static com.example.teachiou.MainActivity.firebaseHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpScreen extends AppCompatActivity {

    EditText emailET, passwordET, nameET;
    String name;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        nameET = findViewById(R.id.editTextName);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(String email, String password) {
        Intent intent = new Intent(this, roleSelection.class);
        name = nameET.getText().toString();
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

                                // add a document to fire store with the users name and their unique UID from auth account
                                firebaseHelper.addUserToFirestore(user.getUid());

                                // This is needed to help with asynchronous method calls in firebase
                                //firebaseHelper.attachReadDataToUser();

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
}
