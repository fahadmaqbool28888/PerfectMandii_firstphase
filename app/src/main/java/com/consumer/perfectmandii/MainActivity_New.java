package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity_New extends AppCompatActivity
{
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    List<ModelDas> modelDasList;
    ContentAdapter contentAdapter;
    RecyclerView recyclerView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView profile,storemame_,logiu;
    CircleImageView prImageView;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit(); // save the changes
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        //
        Toolbar toolbar = findViewById(R.id.toolbar_my);

        navigationView = (NavigationView) findViewById(R.id.main_screen_menu);
       // bottomNavigationView=findViewById(R.id._bnavg);
        setSupportActionBar(toolbar);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        logiu=findViewById(R.id.logiu);
        logiu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       profile=hView.findViewById(R.id.tilename_storename);
       prImageView=hView.findViewById(R.id.detail_iamge);
       storemame_=hView.findViewById(R.id.headname_storename);


        drawerLayout = findViewById(R.id.my_drawer_layout_1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar!=null)
        {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        }
        loadFragment(new HomeFragment());
        //
 /*       modelDasList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyc);
        new AsyncFetch().execute();

        get_Data();*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.mange_order:
                        Intent intent=new Intent(MainActivity_New.this, UserOrderActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.shopping_cart:
                        Intent intent1=new Intent(MainActivity_New.this, shoppingBasket.class);
                        startActivity(intent1);
                        break;
                }


                return true;
            }
        });

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {

            switch (item.getItemId()) {
                case R.id.mange_order:
                    Intent intent=new Intent(MainActivity_New.this, UserOrderActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.shopping_cart:
                    // toolbar.setTitle("Shop");
                    Intent intent1=new Intent(MainActivity_New.this, shoppingBasket.class);
                    startActivity(intent1);
                    return true;

            }
            return false;
        }
    };
    SQLiteHelper sqLiteHelper;
    List<UserModel> userModelList;
    void get_Data()
    {
        sqLiteHelper=new SQLiteHelper(MainActivity_New.this);
        userModelList=sqLiteHelper.readCustomer();

       // Toast.makeText(MainActivity_New.this,String.valueOf(userModelList.size()),Toast.LENGTH_LONG).show();
        if (userModelList.size()>0)
        {
            UserModel pointModel=userModelList.get(0);
            profile.setText(pointModel.shop);
            storemame_.setText(pointModel.Name);
            Glide.with(MainActivity_New.this).load("https://sellerportal.perfectmandi.com/"+pointModel.session).into(prImageView);


        }
        else
        {

            Toast.makeText(MainActivity_New.this,"No DB",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            if (item.getItemId()==R.id.mange_order)
            {
                Toast.makeText(MainActivity_New.this,"Go Away",Toast.LENGTH_LONG).show();
                return true;
            }
            return true;
        }



        return super.onOptionsItemSelected(item);
    }



    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(MainActivity_New.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try
            {


                url = new URL("https://staginigserver.perfectmandi.com/product_index.php");

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
            progressDialog.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ModelDas modelDas=new ModelDas();
                    modelDas.di_name=json_data.getString("di_name");
                    modelDas.di_hname=json_data.getString("di_hname");
                    modelDas.array=json_data.getJSONArray("di_catalog");
                    modelDas.orien=json_data.getString("orientation");
                    modelDas.banner=json_data.getString("banner");
                    modelDasList.add(modelDas);
                }
                contentAdapter = new ContentAdapter(MainActivity_New.this, modelDasList);
                recyclerView.setAdapter(contentAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity_New.this));
            }
            catch (Exception exception)
            {
            }

        }

    }

}