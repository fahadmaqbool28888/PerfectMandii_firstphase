package com.consumer.perfectmandii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.AdapterProductCatalog;
import com.consumer.perfectmandii.Model.DataFish;

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

public class ProductCatalog extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice,mra;
    private AdapterProductCatalog mAdapter;
    TextView textView,catpro;
    String flag;
    String catproname;
    ImageView cartbutton,backbutton;
    boolean isuserlogin;
    String session=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        Intent intent=getIntent();
        flag=intent.getStringExtra("vendor");
        catproname=intent.getStringExtra("name");



        isuserlogin=checkuser();



        cartbutton=findViewById(R.id.cart_button);
        backbutton=findViewById(R.id.backbutton);
        catpro=findViewById(R.id.vend_prod_prov);
        catpro.setText(catproname);



        mRVFishPrice=findViewById(R.id.catalog_product);
        mra=findViewById(R.id.catalog_sproduct);
        textView=findViewById(R.id.itemdes);
        textView.setVisibility(View.INVISIBLE);
        mra.setVisibility(View.INVISIBLE);
        new AsyncFetch().execute();


        cartbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                   if(!isuserlogin)
                   {
                       Intent intent1=new Intent(ProductCatalog.this,LoginActivity.class);
                       startActivity(intent1);

                   }
                   else
                   {

                   }

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    boolean checkuser()
    {
        return false;
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ProductCatalog.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://staginigserver.perfectmandi.com/l3product.php?id="+flag);

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
            pdLoading.dismiss();
            List<DataFish> data=new ArrayList<>();

            pdLoading.dismiss();
            try
            {
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.fishImage= json_data.getString("image_url");
                    fishData.fishName= json_data.getString("name");
                    fishData.catName= json_data.getString("l2_product_name");
                    fishData.status=json_data.getString("status");
                    fishData.vendorprovider=flag;
                    data.add(fishData);
                }
                mAdapter = new AdapterProductCatalog(ProductCatalog.this, data,mra,textView);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(ProductCatalog.this,LinearLayoutManager.HORIZONTAL,false));

            }
            catch (Exception e)
            {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}