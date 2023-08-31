package com.customer.perfectcustomer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.CustomerOrder.IndexViewActivity;
import com.customer.perfectcustomer.Activity.Home.Order.fetch.OrderLogActivity;
import com.customer.perfectcustomer.Activity.Home.Order.fetch.UserOrderActivity;
import com.customer.perfectcustomer.Model.Customer.UserModel;
import com.customer.perfectcustomer.LocalDB.DatabaseClass;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.Home.Fragment.HomeFragment;
import com.customer.perfectcustomer.Activity.Authentication.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_OP extends AppCompatActivity
{
    public DrawerLayout drawerLayout;
    String session,contact;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView profile,storemame_,logiu;
    CircleImageView prImageView;
    TextView logout;

    TextView value,value1;
    CardView numb1_1,numb2_1;
    String  new_yes;
    ImageView searchtype;

    DatabaseClass database;

    private void loadFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    DatabaseClass db;
    ArrayList<UserModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_op);
        db=new DatabaseClass(MainActivity_OP.this);






        Intent intent=getIntent();
        new_yes=intent.getStringExtra("new");

        if (new_yes!=null)
        {

        }
        else
        {

        }

        toolbar=findViewById(R.id.toolbar_12);
        logout=findViewById(R.id.logout);
        setSupportActionBar(toolbar);

        numb1_1=findViewById(R.id.numb1_1);
        numb2_1=findViewById(R.id.numb2_1);
        value=findViewById(R.id.value);
        value1=findViewById(R.id.value1);

        navigationView = findViewById(R.id.main_screen_menu_2);
        bottomNavigationView=findViewById(R.id._bnavg_1);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        profile=hView.findViewById(R.id.tilename_storename);
        prImageView=hView.findViewById(R.id.detail_iamge);
        storemame_=hView.findViewById(R.id.headname_storename);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_name);
        }
        else
        {

        }

        //


          get_Data();

       new  getPayValue().execute();
        if (session!=null)
        {
            logout.setText("Log out");
        }
        else
        {
            logout.setText("Sign in");
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logout.getText().equals("Log out"))
                {

                    db.delete();
                    get_Data();
                    finish();
                    startActivity(getIntent());
                }
                else
                {
                    Intent intent=new Intent(MainActivity_OP.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        loadFragment(new HomeFragment());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.mange_order:
                        Intent intent=new Intent(MainActivity_OP.this, OrderLogActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.shopping_cart:
                        Intent intent1=new Intent(MainActivity_OP.this, IndexViewActivity.class);
                        startActivity(intent1);
                        break;
                }


                return true;
            }
        });


        searchtype=findViewById(R.id.search_type);
        searchtype.setVisibility(View.INVISIBLE);
        searchtype.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


            }
        });

      //  startService(new Intent( this, connectDataBase.class ) );

    }

    private void get_Data()
    {
        arrayList=db.arrayList();

        if (arrayList.size()>0)
        {


            UserModel pointModel=arrayList.get(0);
            profile.setText(pointModel.shop);
            storemame_.setText(pointModel.Name);
            contact=pointModel.contact;
            session=pointModel.profilepic;
            Glide.with(MainActivity_OP.this).load("https://sellerportal.perfectmandi.com/"+pointModel.profilepic).into(prImageView);

            new AsyncFetch().execute();


        }
        else
        {

        }
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch (item.getItemId()) {
                case R.id.mange_order:
                    Intent intent=new Intent(MainActivity_OP.this, UserOrderActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.shopping_cart:
                    Intent intent1=new Intent(MainActivity_OP.this, IndexViewActivity.class);
                    startActivity(intent1);

                    return true;

            }
            return false;
        }
    };

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(MainActivity_OP.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
        //    progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL("https://sellerportal.perfectmandi.com/get_incart.php?id="+contact);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result)
        {


            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);

                    str=json_data.getString("id");
                    rst=json_data.getString("rid");

                    int a=Integer.parseInt(str);
                    int b=Integer.parseInt(rst);
                    int total=a+b;
                    numb1_1.setVisibility(View.VISIBLE);
                    value.setText(String.valueOf(total));

                    if (total<1)
                    {
                        numb1_1.setVisibility(View.GONE);
                    }


                }

            }
            catch (Exception exception)
            {
            }

        }

    }
    private class getPayValue extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(MainActivity_OP.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            //    progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL("https://sellerportal.perfectmandi.com/get_walletvalue.php?id="+contact);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try
            {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result)
        {


            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);

                    str=json_data.getString("id");

                    int a=Integer.parseInt(str);
                    if (a>0)
                    {
                        numb1_1.setVisibility(View.VISIBLE);
                        value.setText(str);
                    }
                    else
                    {
                        numb1_1.setVisibility(View.GONE);
                    }

                }

            }
            catch (Exception exception)
            {

            }

        }

    }
    String str,rst;

}