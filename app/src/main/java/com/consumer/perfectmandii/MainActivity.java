package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.ContentAdapter;
import com.consumer.perfectmandii.Model.ModelDas;
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

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public String server_Url = "https://staginigserver.perfectmandi.com/product_index.php";

    RecyclerView recyclerView;
    ImageView imageview;
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    HttpURLConnection conn;
    URL url = null;
    Context context;
    String urls;

    ProgressDialog progressDialog;
    ImageView imageView;
    ContentAdapter contentAdapter;

    List<ModelDas> modelDasList;




    TextView textname_1;
    TextView tile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*








        modelDasList = new ArrayList<>();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_my);

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_screen_menu);
        setSupportActionBar(toolbar);
        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);

         textname_1=hView.findViewById(R.id.tilename_storename);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.stage13);
        }


        init();
        // new ContentController(recyclerView,this,server_Url).execute();

        new AsyncFetch().execute();
        get_Data();*/

    }


    CircleImageView profile_image;
    TextView textname,textstore;
    NavigationView navigationView;
    void init() {

        recyclerView = findViewById(R.id.main_Container_mainscreen);
        imageview = findViewById(R.id.main_banner_mainscreen);
        navigationView=findViewById(R.id.main_screen_menu);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        profile_image = (CircleImageView) hView.findViewById(R.id.detail_iamge);
        textname=hView.findViewById(R.id.headname_storename);
        textstore=hView.findViewById(R.id.tilename_storename);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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
            try {


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

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
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
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            Toast.makeText(MainActivity.this,result,Toast.LENGTH_LONG).show();

            try {


                JSONArray jArray = new JSONArray(result);


                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ModelDas modelDas=new ModelDas();
                    modelDas.di_hname=json_data.getString("di_hname");
                   // modelDas.array=json_data.getJSONArray("di_catalog");
                    modelDasList.add(modelDas);
                }
                contentAdapter = new ContentAdapter(context, modelDasList);

                recyclerView.setAdapter(contentAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));


            } catch (Exception exception) {

            }

        }

    }


}