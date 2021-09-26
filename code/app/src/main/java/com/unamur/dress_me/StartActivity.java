package com.unamur.dress_me;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    /**
     * @author Amélie Dieudonné
     * @param savedInstanceState Previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Set the time before fading
        int SPLASH_TIME_OUT = 2500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Run the game
                Intent runGame = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(runGame);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
