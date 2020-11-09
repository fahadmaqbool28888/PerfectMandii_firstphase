package com.consumer.perfectmandii;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity
{
    private static final int SPLASH_TIME_OUT = 3000;
    String dataanndtime=null;
    int height,width;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


       // getdimension();





        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //Intent i = new Intent(SplashScreenActivity.this, Product_Profile_extend.class);
              //  Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                Intent i = new Intent(SplashScreenActivity.this, MainActivity_OP.class);
                 startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }



}