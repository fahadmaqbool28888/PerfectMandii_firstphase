package com.consumer.perfectmandii;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.consumer.perfectmandii.Model.StoreModel;

import java.util.Calendar;
import java.util.List;

public class ProfileInformation extends AppCompatActivity
{

    FragmentPagerAdapter adapterViewPager;
    List<StoreModel> storelist;
    ImageView imageView,imageView2,imageView3,imageView4;
    String fullname, mobilenumber, name, shippingaddress, billingaddress, session, userid, username,store, usernumber,provincev,getContactperson;


    String  createsession()
    {
        Calendar cal = Calendar.getInstance();
        String cs=cal.getTime().toString();
        String  input = cs;
        input = input.replace(" ", "");
        String input1 = input.replace("+", "");
        String input2 = input1.replace(":", "");
        return input2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
       /* ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);*/
        //loadFragment(new StoreFragment());
        intializeWidget();


        Intent getdata = getIntent();

        userid=getdata.getStringExtra("userid");
        name=getdata.getStringExtra("username");
        session=createsession();
        // Storing data into SharedPreferences
        @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getApplication().getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
        myEdit.putString("userid", userid);
        myEdit.putString("name", userid);
        myEdit.putString("session", session);


        myEdit.commit();
        Toast.makeText(ProfileInformation.this,userid,Toast.LENGTH_LONG).show();






        if(session==null)
        {
            Intent intent1=new Intent(ProfileInformation.this, LoginActivity.class);
            startActivity(intent1);
        }
        else {

        }
    }

    private void intializeWidget()
    {
        imageView=findViewById(R.id.stage_1);
        imageView2=findViewById(R.id.stage_2);
        imageView3=findViewById(R.id.stage_3);

    }

    @Override
    protected void onResume() {
        super.onResume();




    }


    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }



    public   void Recieve_Data(String stageone, List<StoreModel> storelist)
    {
        if (stageone.equalsIgnoreCase("Done_1"))
        {
            imageView.setImageResource(R.drawable.step_tick);
        }
        else  if (stageone.equalsIgnoreCase("Done_2"))
        {
            imageView2.setImageResource(R.drawable.step_tick);
        }
        else  if (stageone.equalsIgnoreCase("Done_3"))
        {
            imageView3.setImageResource(R.drawable.step_tick);
        }
        else
        {
            imageView4.setImageResource(R.drawable.step_tick);
        }




    }
}