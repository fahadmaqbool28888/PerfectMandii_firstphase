package com.vendor.perfectmandii;

import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.View;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;


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

public class MainActivity extends AppCompatActivity
{


   String userid,name,session, path;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    CircleImageView circleImageView;
    TextView textname_store;
    RecyclerView mRVFishPrice;
    AdapterHome mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);



        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        name=intent.getStringExtra("name");
        session=intent.getStringExtra("session");
        path=intent.getStringExtra("path");


        if (session==null)
        {
            Intent intent1=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent1);
        }
        else
        {

            intializeWidget();
        /*    Picasso.get().load(path).into(circleImageView);
            textname_store.setText(name);*/
            new AsyncFetch().execute();
        }




    }

    void  intializeWidget()
    {
     /*   circleImageView=findViewById(R.id.avatar_store);
        textname_store=findViewById(R.id.textname_store);*/

        mRVFishPrice=findViewById(R.id.pperfect_mandi);
       // logout=findViewById(R.id.sidnav_store);
    }


  public   void logout(View view)
    {

        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
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
                url = new URL("https://spoyer.ru/api/en/get.php?login=ayna&token=12784-OhJLY5mb3BSOx0O&task=livedata&sport=soccer");
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
            System.out.println(result);
/*
            List<vendorServiceModel> data=new ArrayList<>();


            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    //JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(i);
                    vendorServiceModel fishData = new vendorServiceModel();
                    fishData.serviceicon= json_data.getString("serviceicon");
                    fishData.servicename= json_data.getString("servicename");
                    fishData.usertype= json_data.getString("usertype");
                    fishData.servicestatus=json_data.getString("status");
                    fishData.userid=userid;
                    fishData.name=name;
                    fishData.session=session;



                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterHome(MainActivity.this, data);
                progressDialog.dismiss();
                mRVFishPrice.setAdapter(mAdapter);
                progressDialog.dismiss();
                mRVFishPrice.setLayoutManager( new GridLayoutManager(MainActivity.this,3));

            } catch (Exception e)
            {

            }*/

        }

    }

}