package com.example.teachiou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class loadScreenActivity extends AppCompatActivity {

    //the video we used to create a splash/load screen
    // https://youtu.be/u_vLGtKDDgc

    //private boolean switchOn= false;
    private static int SPLASH_SCREEN = 4500;

    private LottieAnimationView lottieLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        lottieLoad = findViewById(R.id.lottieLoad);

//        lottieLoad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(switchOn){
//                    lottieLoad.setMinAndMaxProgress(0.5f, 1.0f);
//                    lottieLoad.playAnimation();
//                    switchOn = false;
//                }else{
//                    lottieLoad.setMinAndMaxProgress(0.0f, 0.5f);
//                    lottieLoad.playAnimation();
//                    switchOn = true;
//                }
//            }
//        });

        lottieLoad.setMinAndMaxProgress(0.5f, 1.0f);
        lottieLoad.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(loadScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}