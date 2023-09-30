package com.vendor.perfectmandii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderDashboardActivity extends AppCompatActivity
{

    Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String session,categoryname,id,userid;


    ImageView bacl_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        Intent intent = getIntent();
        session = intent.getStringExtra("session");
        categoryname=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        userid=intent.getStringExtra("userid");

        if (session.equalsIgnoreCase("")) {
            Intent intent1 = new Intent(OrderDashboardActivity.this, LoginActivity.class);
            startActivity(intent1);
        }


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        bacl_icon=findViewById(R.id.bacl_icon);

        bacl_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private void setupViewPager(ViewPager viewPager) {
      ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyorderFragment(userid,session,categoryname), "New"+"\n"+"Orders");
        adapter.addFragment(new topayFragment(userid), "Orders"+"\n"+"To Ship");
        adapter.addFragment(new toshipFragment(session,categoryname), " Orders"+"\n"+"Shipped");
        adapter.addFragment(new myreturnsFragment(session,categoryname), "Orders "+"\n"+"Returned");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);

        }
    }




}