package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class testfile extends AppCompatActivity
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    String session,categoryname,id,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfile);




    }



}