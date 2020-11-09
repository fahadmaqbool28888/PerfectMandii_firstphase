package com.consumer.perfectmandii.Activity.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.consumer.perfectmandii.R;

public class NotificationActivity extends AppCompatActivity
{

    ImageView backsc;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //1
        intializewidget();
        //2
        intialize();
    }

    void intializewidget()
    {
        backsc=findViewById(R.id.bax_noti);
    }

    void intialize()
    {
        backsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}