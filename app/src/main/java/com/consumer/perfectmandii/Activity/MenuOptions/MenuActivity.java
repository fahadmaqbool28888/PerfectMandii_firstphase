package com.consumer.perfectmandii.Activity.MenuOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.perfectmandii.Activity.User.customerprofile.CustomerDashboard;
import com.consumer.perfectmandii.BankDetailActivity;
import com.consumer.perfectmandii.DataBase.DatabaseClass;
import com.consumer.perfectmandii.LoginActivity;
import com.consumer.perfectmandii.MainActivity;
import com.consumer.perfectmandii.R;
import com.consumer.perfectmandii.Room.EntityClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity
{
    String session,userid,path,name;
    private List<EntityClass> list;
    CardView cardView,profile,order,shipping,paymentdetail,logout;
    ImageView backbutton_menu;
    CircleImageView circleImageView;
    TextView profilename_;
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
           getData();
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
                    finish();
                }
            });
            logout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
        backbutton_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getData()
    {
        if (session!=null)
        {


            list = new ArrayList<>();
            list = DatabaseClass.getDatabase(getApplicationContext()).getDao().getAllData();

            Picasso.get().load(list.get(0).getImagepath()).into(circleImageView);
            profilename_.setText(list.get(0).getUsername());

          //  Picasso.get().load(list.get(0).getImagepath()).into(circleImageView);
        }
        else
        {
            profilename_.setText("Hello, Sign In");
            circleImageView.setVisibility(View.INVISIBLE);
        }




    }
    void intialize_widget()
    {
       /* profilename_=findViewById(R.id.profilename_);
        //
        circleImageView=findViewById(R.id.disp_menu);
        backbutton_menu=findViewById(R.id.backbutton_menu);
        cardView=findViewById(R.id.home_Screen);
        profile=findViewById(R.id.card_profile);
        order=findViewById(R.id.card_order);
        shipping=findViewById(R.id.card_shipping);
        paymentdetail=findViewById(R.id.card_pdetaIL);
        logout=findViewById(R.id.logout);*/
    }
}