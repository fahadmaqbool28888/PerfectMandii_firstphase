package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.hbb20.CountryCodePicker;

public class SignupActivity extends AppCompatActivity {
    String countryCodeAndroid = "92";
    CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ccp=findViewById(R.id.ccp);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCodeAndroid = ccp.getSelectedCountryCode();
                Log.d("Country Code", countryCodeAndroid);
            }
        });

    }
}