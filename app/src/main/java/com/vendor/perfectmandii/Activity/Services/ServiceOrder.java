package com.vendor.perfectmandii.Activity.Services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vendor.perfectmandii.R;

public class ServiceOrder extends Service
{
    public ServiceOrder()
        {

        }
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(this, R.raw.doorbell);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100, 100);
        //mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "Playing Bohemian Rashpody in the Background",    Toast.LENGTH_SHORT).show();
        return startId;
    }
    public void onStart(Intent intent, int startId) {
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }
}