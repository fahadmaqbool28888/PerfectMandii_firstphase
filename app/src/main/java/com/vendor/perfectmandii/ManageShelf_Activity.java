package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.DashboardAdapter.ProductShelfAdapter;
import com.vendor.perfectmandii.Adapter.DashboardAdapter.dashboardAdapter;
import com.vendor.perfectmandii.Model.ProductShelf;
import com.vendor.perfectmandii.Model.userVendor;

import androidx.appcompat.app.ActionBar;

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


public class ManageShelf_Activity extends AppCompatActivity
{
    private Toolbar mTopToolbar;
    String userid,name,session, path,store;
    ProductShelfAdapter productShelfAdapter;
    ImageView bax;
    /*addproductIntent.putExtra("userid",userid);
                addproductIntent.putExtra("username",name);
                addproductIntent.putExtra("session",session);*/
    RecyclerView product_shelf;

    ArrayList<userVendor> pointModelsArrayList;
    DBHelper dbHandler;

    String storeid;
    void getData(Context context)
    {
        dbHandler=new DBHelper(context);
        pointModelsArrayList=dbHandler.readVendor();
        userVendor vendor=pointModelsArrayList.get(0);

        storeid=vendor.getStoreid();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shelf2);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
// Customize the back button
    /*    actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_keyboard_backspace_24);
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        name=intent.getStringExtra("username");
        session=intent.getStringExtra("session");
        init_widget();
        getData(ManageShelf_Activity.this);
       Toast.makeText(ManageShelf_Activity.this,storeid,
               Toast.LENGTH_LONG).show();
        // mTopToolbar =findViewById(R.id.my_toolbar);

        new AsyncFetch().execute();

        bax=findViewById(R.id.bac_icon);
        bax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void init_widget()
    {
        product_shelf=findViewById(R.id.productshelf_);

    }
    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(ManageShelf_Activity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                url = new URL("https://sellerportal.perfectmandi.com/get_inventoryDetail.php?id="+storeid);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
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


            ArrayList<ProductShelf> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    ProductShelf modeldashboard = new ProductShelf();
                    modeldashboard.id= json_data.getString("id");
                    modeldashboard.image_path= json_data.getString("image_path");
                    modeldashboard.Product_Name=json_data.getString("Product_Name");
                    modeldashboard.Product_Description=json_data.getString("Product_Description");
                    modeldashboard.Product_Unit_Price=json_data.getString("Product_Unit_Price");
                    modeldashboard.Product_Unit_Quan=json_data.getString("stock");
                    modeldashboard.MOQ=json_data.getString("MOQ");
                    modeldashboard.provider=json_data.getString("Product_Provider");
                    modeldashboard.lisiting_status= json_data.getString("lisiting_status");
                    if(json_data.getString("lisiting_status").equalsIgnoreCase("active"))
                    {

                        modeldashboard.setChecked(true);
                    }
                    else
                    {

                        modeldashboard.setChecked(false);

                    }



                    modeldashboard.store_listing= json_data.getString("store_listing");
                    modeldashboard.userid=userid;

if (modeldashboard.Product_Name.equalsIgnoreCase(""))
{

}
else {
    data.add(modeldashboard);
}


                }

                // Setup and Handover data to recyclerview

                productShelfAdapter = new ProductShelfAdapter(ManageShelf_Activity.this, data);
                progressDialog.dismiss();
                product_shelf.setAdapter(productShelfAdapter);

                product_shelf.setLayoutManager( new LinearLayoutManager(ManageShelf_Activity.this

                ));

            } catch (Exception e)
            {

            }


        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}