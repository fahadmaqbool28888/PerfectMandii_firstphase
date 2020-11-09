package com.consumer.perfectmandii;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Activity.Order.fetch.UserOrderActivity;
import com.consumer.perfectmandii.Adapter.ContentAdapter;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.Fragment_Update.HomeFragment;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.ModelDas;
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
import java.util.List;


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
    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit(); // save the changes
    }




    TextView value;
    CardView numb1_1;
    String  new_yes;
    void showDialog()
    {
        new AlertDialog.Builder(MainActivity_OP.this)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_op);
        Intent intent=getIntent();
        new_yes=intent.getStringExtra("new");
        if (new_yes!=null)
        {
            Toast.makeText(MainActivity_OP.this, "Welcome to PerfectMandi", Toast.LENGTH_SHORT).show();
            //showDialog();
        }
        sqLiteHelper=new SQLiteHelper(MainActivity_OP.this);
        toolbar=findViewById(R.id.toolbar_12);
        logout=findViewById(R.id.logout);
        setSupportActionBar(toolbar);

        numb1_1=findViewById(R.id.numb1_1);

        value=findViewById(R.id.value);

        navigationView = (NavigationView) findViewById(R.id.main_screen_menu_2);
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

        if(actionBar != null){
            // to make the Navigation drawer icon always appear on the action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_name);
        }

        get_Data();


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

                    sqLiteHelper.deleteCustomer(contact);
                    get_Data();
                    finish();
                    startActivity(getIntent());
                }
                else
                {
                    Intent intent=new Intent(MainActivity_OP.this,LoginActivity.class);
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
                        Intent intent=new Intent(MainActivity_OP.this, UserOrderActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.shopping_cart:
                        Intent intent1=new Intent(MainActivity_OP.this, shoppingBasket.class);
                        startActivity(intent1);
                        break;
                }


                return true;
            }
        });
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



    SQLiteHelper sqLiteHelper;
    List<UserModel> userModelList;
    void get_Data()
    {

        userModelList=sqLiteHelper.readCustomer();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            profile.setText(pointModel.shop);
            storemame_.setText(pointModel.Name);
            contact=pointModel.contact;
            session=pointModel.profilepic;
            Glide.with(MainActivity_OP.this).load("https://sellerportal.perfectmandi.com/"+pointModel.session).into(prImageView);


            new AsyncFetch().execute();
        }
        else
        {



        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
                    Intent intent1=new Intent(MainActivity_OP.this, shoppingBasket.class);
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
    String str;

}