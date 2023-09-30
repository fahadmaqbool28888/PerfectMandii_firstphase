package com.vendor.perfectmandii.Activity;

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
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.vendor.perfectmandii.Adapter.AdapterProductUpdatePrice;
import com.vendor.perfectmandii.CategoryDetail.Activity.ByCategoryActivity;
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

public class PriceUpdateActivity extends AppCompatActivity {
    private AdapterProductUpdatePrice mAdapter;
    String json_string;
    int pos;
    List<String> id_list,price_list,newprice_list,racposition;

    RecyclerView recyclerView;

    String price,newprice,id;

    CardView checkoutforshopping;
    DBHelper dbHelper;
    String vendor;
    ArrayList<userVendor> pointModelsArrayList;

    ImageView bax;
    ImageView changePrice;
    void init()
    {
        pointModelsArrayList = dbHelper.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            vendor=pointModel.userid;




        }
        else
        {


        }




    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_update);
        bax=findViewById(R.id.bax);
        changePrice=findViewById(R.id.changePrice);

        bax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PriceUpdateActivity.this,ByCategoryActivity.class));
            }
        });
        recyclerView=findViewById(R.id.nope_12);
        dbHelper=new DBHelper(PriceUpdateActivity.this);
        //
        init();


        new AsyncFetch().execute();

        //
        id_list=new ArrayList<>();
        price_list=new ArrayList<>();
        newprice_list=new ArrayList<>();
        racposition=new ArrayList<>();
        //

        //



        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

    }
    //
    String JsonString() {

        int i;


        json_string=null;



        try {
            //Repeat and loop this until all objects are added (and add try+catch)
            json_string ="{\"upload_fishes\":[";
            for (i=0;i<racposition.size();i++)
            {

                JSONObject obj_new = new JSONObject();



                obj_new.put("productid",id_list.get(i));
                obj_new.put("price",newprice_list.get(i));

                json_string = json_string + obj_new.toString() + ",";

//Close JSON string

            }
            json_string = json_string.substring(0, json_string.length()-1);
            json_string += "]}";



        }
        catch (JSONException jsox)
        {
            Toast.makeText(PriceUpdateActivity.this,jsox.toString(),Toast.LENGTH_LONG).show();
        }

        return  json_string;
    }
    //
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String flag=intent.getStringExtra("flag");
            price=intent.getStringExtra("price");
            newprice=intent.getStringExtra("newprice");
            id=intent.getStringExtra("id");
            pos=intent.getIntExtra("pos",0);

            if(flag.equalsIgnoreCase("exclude"))
            {


                id_list.remove(id);
                price_list.remove(price);
                newprice_list.remove(newprice);

                racposition.remove(String.valueOf(pos));

            }
            else
            {
                id_list.add(id);
                price_list.add(price);
                newprice_list.add(newprice);
                racposition.add(String.valueOf(pos));

                String string=JsonString();
                System.out.println(string);

                try {
                    new AsyncFetch_add().execute();

                }
                catch (Exception e)
                {

                }
            }

        }
    };
    //
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(PriceUpdateActivity.this);
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
                url = new URL("https://sellerportal.perfectmandi.com/get_ProductPrice.php?id="+vendor);

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
            List<StockProductModel> data=new ArrayList<>();


            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    StockProductModel stockProductModel=new StockProductModel();
                    stockProductModel.id=json_data.getString("id");
                    stockProductModel.price=json_data.getString("price");

                    stockProductModel.name=json_data.getString("name");
                    stockProductModel.path=json_data.getString("path");




                    // fishData.usid=username;

                    data.add(stockProductModel);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdapterProductUpdatePrice(PriceUpdateActivity.this, data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager( new LinearLayoutManager(PriceUpdateActivity.this));


            } catch (Exception e) {
            }
        }

    }

    private class AsyncFetch_add extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(PriceUpdateActivity.this);
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

                String operatingurl="https://sellerportal.perfectmandi.com/update_product_price.php?data="+json_string;

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

            if (result.contains("Price Updated"))
            {
               mAdapter.reomve_item(pos);
            }



        }

    }
}