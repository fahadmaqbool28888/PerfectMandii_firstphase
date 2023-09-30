package com.vendor.perfectmandii.Activity.User.customerprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.R;
import com.google.android.material.tabs.TabLayout;

public class CustomerDashboard extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView editprofile;

    String session,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);


        Intent getdata=getIntent();
        session=getdata.getStringExtra("session");
        userid=getdata.getStringExtra("userid");


        if (session==null)
        {
            Intent intent1=new Intent(CustomerDashboard.this, LoginActivity.class);
            startActivity(intent1);
        }
        else
        {
            intializewidget();

            editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent=new Intent(CustomerDashboard.this,EditProfileActivity.class);
                    intent.putExtra("session",session);
                    intent.putExtra("userid",userid);
                    startActivity(intent);

                }
            });
        }

    }

    private void intializewidget()
    {
        editprofile=findViewById(R.id.editprofile);
    }


}