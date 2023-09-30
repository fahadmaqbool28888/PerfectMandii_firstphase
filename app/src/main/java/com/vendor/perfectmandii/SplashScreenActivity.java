package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.vendor.perfectmandii.Activity.User.customerprofile.EditProfileActivity;
import com.vendor.perfectmandii.Dashboard.OPActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);

           //     Intent i = new Intent(SplashScreenActivity.this, OPActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}