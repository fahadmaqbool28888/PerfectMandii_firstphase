package com.consumer.perfectmandii;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.Product_Before_Login;
import com.consumer.perfectmandii.ProfileArea.CreditionalsFragment;

import java.util.ArrayList;

public class ProfileInformationActivity extends AppCompatActivity
{

    ImageView imageView,imageView2,imageView3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);
intializeWidget();
get_Data_3();
        loadFragment(new CreditionalsFragment());
    }
    private void intializeWidget()
    {
        imageView=findViewById(R.id.stage_1);
        imageView2=findViewById(R.id.stage_2);
        imageView3=findViewById(R.id.stage_3);

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



    public   void Recieve_Data(String stageone)
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




    }



    ArrayList<Product_Before_Login> codearray;
    SQLiteHelper sqLiteHelper;
    void get_Data_3()
    {
        sqLiteHelper=new SQLiteHelper(ProfileInformationActivity.this);
        codearray=sqLiteHelper.readBeforeProduct();



        if (codearray.size()>0)
        {
            Product_Before_Login product_before_login=codearray.get(0);






        }
        else
        {


        }
    }
}