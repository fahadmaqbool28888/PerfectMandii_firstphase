package com.consumer.perfectmandii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TextView part1;
    String session,categoryname,id,userid,name,profilepic;

    ImageView back;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        back=findViewById(R.id.backbutton);

        part1=findViewById(R.id.part1);
        Intent intent = getIntent();
       /* session = intent.getStringExtra("session");*/
        categoryname=intent.getStringExtra("cname");

        String str=categoryname.replaceAll("[^a-zA-Z]"," ");
        part1.setText(str);
     /*   id=intent.getStringExtra("id");
        userid=intent.getStringExtra("userid");
        name=intent.getStringExtra("name");
        profilepic=intent.getStringExtra("profilepic");*/


     System.out.println(" String is  "+categoryname);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(DashboardActivity.this, MainActivity_OP.class);
                //intent1.putExtra("session",session);
                intent1.putExtra("name",categoryname);
               // intent1.putExtra("id",id);
              //  intent1.putExtra("userid",userid);
               // intent1.putExtra("name",name);
               // intent1.putExtra("path",profilepic);
                startActivity(intent1);
            }
        });

     /*   if (session.equalsIgnoreCase("")) {
            Intent intent1 = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent1);
        }*/


        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(DashboardActivity.this,MainActivity_OP.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Product(session,categoryname,userid,id,name,profilepic), "Product");
        adapter.addFragment(new vendor(categoryname), "Vendor");

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
