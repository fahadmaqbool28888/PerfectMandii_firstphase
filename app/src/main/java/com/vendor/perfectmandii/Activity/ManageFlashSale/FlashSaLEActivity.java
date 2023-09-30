package com.vendor.perfectmandii.Activity.ManageFlashSale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.ManageFlashSale.Adapter.StatusAdapter;
import com.vendor.perfectmandii.Activity.ManageFlashSale.model.FlashSaleModel;
import com.vendor.perfectmandii.Activity.PriceUpdateActivity;
import com.vendor.perfectmandii.Adapter.AdapterProductUpdatePrice;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
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

public class FlashSaLEActivity extends AppCompatActivity {
    //
    String vendor,id,pro;
    DBHelper dbHelper;
    ArrayList<userVendor> pointModelsArrayList;
    StatusAdapter setAdapter;
    RecyclerView nope_12;
    List<String> id_list,price_list,newprice_list,racposition;
    String json_string;
    ImageView bax;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sa_leactivity);
        dbHelper=new DBHelper(FlashSaLEActivity.this);
        init();
        nope_12=findViewById(R.id.nope_12);
        bax=findViewById(R.id.bax);

        bax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new AsyncFetch().execute();

        id_list=new ArrayList<>();
        racposition=new ArrayList<>();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }
    //
  /*  String JsonString() {

        int i;


        json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<racposition.size();i++)
            {

                JSONObject obj_new = new JSONObject();



                obj_new.put("productid",id_list.get(i));
                obj_new.put("provider",pro);


                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(FlashSaLEActivity.this,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }*/
    //
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent


            try {
                String flag=intent.getStringExtra("flag");

                id=intent.getStringExtra("id");
                int pos=intent.getIntExtra("pos",0);


                id_list.add(id);
                racposition.add(String.valueOf(pos));


                Toast.makeText(FlashSaLEActivity.this,String.valueOf(id),Toast.LENGTH_LONG).show();


                new AsyncFetch_add().execute();
            }
            catch (Exception exception)
            {

            }
               // new PriceUpdateActivity.AsyncFetch_add().execute();


        }
    };
    void init()
    {
        pointModelsArrayList = dbHelper.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            vendor=pointModel.userid;
            pro=pointModel.storeid;




        }
        else
        {


        }

      /*  userVendor pointModel=pointModelArrayList.get(0);

        tokenValue.setText(pointModel.points);*/
//


    }


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(FlashSaLEActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setIcon(R.drawable.optimizedlogo);
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {


                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://sellerportal.perfectmandi.com/get_FlashSaleProduct.php?id="+vendor);

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

            //this method will be running on UI thread


            System.out.println(result);
            pdLoading.dismiss();
            // System.out.println(result);
            List<FlashSaleModel> data=new ArrayList<>();


            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    FlashSaleModel flashSaleModel=new FlashSaleModel();
                    flashSaleModel.id=json_data.getString("id");
                    flashSaleModel.saleprice=json_data.getString("price");

                    flashSaleModel.product_name=json_data.getString("name");
                    flashSaleModel.image_path=json_data.getString("path");
                    flashSaleModel.status=json_data.getString("status");




                    // fishData.usid=username;

                    data.add(flashSaleModel);
                }

                // Setup and Handover data to recyclerview

                setAdapter = new StatusAdapter(FlashSaLEActivity.this, data);
                nope_12.setAdapter(setAdapter);
                nope_12.setLayoutManager( new LinearLayoutManager(FlashSaLEActivity.this));


            } catch (Exception e) {
            }
        }

    }

    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(FlashSaLEActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()

        {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params)
        {

            try {

                String operatingurl="https://sellerportal.perfectmandi.com/remove_flash_sale.php?id="+id;

                System.out.println(operatingurl);
                url = new URL(operatingurl);

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
        protected void onPostExecute(String result)
        {
            System.out.println(result);

            pdLoading.dismiss();

            if (result.contains("Sale Record Updated"))
            {
                finish();
                startActivity(getIntent());
            }



        }

    }




}