package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;

public class loadScreenActivity extends AppCompatActivity {

    private boolean switchOn= false;

    LottieAnimationView lottieLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        lottieLoad = findViewById(R.id.lottieLoad);

        lottieLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switchOn){
                    lottieLoad.setMinAndMaxProgress(0.5f, 1.0f);
                    lottieLoad.playAnimation();
                    switchOn = false;
                }else{
                    lottieLoad.setMinAndMaxProgress(0.0f, 0.5f);
                    lottieLoad.playAnimation();
                    switchOn = true;
                }
            }
        });
    }
}