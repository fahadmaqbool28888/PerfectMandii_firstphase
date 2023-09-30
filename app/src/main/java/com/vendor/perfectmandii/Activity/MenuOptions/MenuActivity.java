package com.vendor.perfectmandii.Activity.MenuOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.User.customerprofile.CustomerDashboard;
import com.vendor.perfectmandii.BankDetailActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.R;

public class MenuActivity extends AppCompatActivity
{
    String session,userid;

    CardView cardView,profile,order,shipping,paymentdetail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        userid = intent.getStringExtra("userid");

        if (session==null)
        {
            Intent intent1=new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
            {
            intialize_widget();
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
            profile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MenuActivity.this, CustomerDashboard.class);
                    intent.putExtra("session", session);
                    intent.putExtra("userid", userid);
                    startActivity(intent);

                }
            });

            order.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {


                    Toast.makeText(MenuActivity.this,"This feature added soon",Toast.LENGTH_LONG).show();
                }
            });
            shipping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuActivity.this,"This feature added soon",Toast.LENGTH_LONG).show();
                }
            });
            paymentdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuActivity.this, BankDetailActivity.class);
                    intent.putExtra("session", session);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
            });
        }
    }

    void intialize_widget()
    {
        cardView=findViewById(R.id.home_Screen);
        profile=findViewById(R.id.card_profile);
        order=findViewById(R.id.card_order);
        shipping=findViewById(R.id.card_shipping);
        paymentdetail=findViewById(R.id.card_pdetaIL);
    }
}