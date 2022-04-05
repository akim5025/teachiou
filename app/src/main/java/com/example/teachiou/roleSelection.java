package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class roleSelection extends AppCompatActivity {

    public static final String EXTRA_ROLE = "com.example.teachiou.ROLE";
    public String role;
    public static FirebaseHelper firebaseHelper = new FirebaseHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_role);
    }

    public void onRadioButtonClicked(View view) {
        // https://developer.android.com/guide/topics/ui/controls/radiobutton
        // Is the button now checked?
        RadioButton studentRad = findViewById(R.id.student);
        RadioButton teacherRad = findViewById(R.id.teacher);
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.teacher:
                if (checked){
                    studentRad.setChecked(false);
                    role = "teacher";

                    firebaseHelper.addRole(role, firebaseHelper.getmAuth().getUid());

                }
                break;
            case R.id.student:
                if (checked){
                    teacherRad.setChecked(false);
                    role = "student";

                    firebaseHelper.addRole(role, firebaseHelper.getmAuth().getUid());
                    
                }
                break;
        }
    }

    public void onSelected(View view){
        Intent intent = new Intent(this, ClassSelection.class);
        intent.putExtra(EXTRA_ROLE, role);
        startActivity(intent);
    }


}