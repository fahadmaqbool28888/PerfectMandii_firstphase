package com.vendor.perfectmandii.Reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

public class MyReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        //speedspeedspeed
        double speed = Double.valueOf(intent.getStringExtra("speed"));
        double altitude = Double.valueOf(intent.getStringExtra("altitude"));
        // String city=intent.getStringExtra(longitude);
        System.out.println(longitude);
        System.out.println("broadcast latitude:" + latitude);
        System.out.println("broadcast speed:" + speed);
        System.out.println("broadcast altitude:" + altitude);
        intent = new Intent("custom-message");





        intent.putExtra("lat",String.valueOf(latitude));
        intent.putExtra("lng",String.valueOf(longitude));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);



        //  Toast.makeText(MainActivity.this,,Toast.LENGTH_LONG).show();
           /*     tv_area.setText(addresses.get(0).getAdminArea());
                tv_locality.setText(stateName);
                tv_address.setText(countryName);*/




        //  double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        //  Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }
}