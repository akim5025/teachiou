package com.example.teachiou;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.collect.ArrayListMultimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassSelection extends AppCompatActivity {

    private int totalClasses = 2;
    ArrayList<CharSequence> usersClasses = new ArrayList();
    Map<String, String> classHash = new HashMap<String, String>();
    public static FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_classes);

        Intent intent = getIntent();
        String role = intent.getStringExtra(roleSelection.EXTRA_ROLE);
    }

    public void onClassSelected(View v){
        ViewGroup layout = (ViewGroup)findViewById(R.id.layout_checkList);
        usersClasses.clear();

        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if(child instanceof CheckBox)
            {
                CheckBox box = (CheckBox) child;
                if(box.isChecked()) {
                    usersClasses.add(box.getText());

                    Snackbar errorSnack = Snackbar.make(v, "Classes - " + usersClasses.size(), Snackbar.LENGTH_SHORT);
                    errorSnack.show();
                }
            }
        }

        for (int i = 0; i < usersClasses.size(); i++) {
            classHash.put(Integer.toString(i), usersClasses.get(i).toString());
        }
        firebaseHelper.editData(classHash, "CLASSES");


        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }


}