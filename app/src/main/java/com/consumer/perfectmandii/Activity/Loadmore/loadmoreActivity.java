package com.consumer.perfectmandii.Activity.Loadmore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.SubCatogories.subItemAdapter_More;
import com.consumer.perfectmandii.MainActivity_OP;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;
import com.consumer.perfectmandii.R;

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

public class loadmoreActivity extends AppCompatActivity
{
    JSONArray data;
    String userid,session,jsonArray,categoryname,l1;


    RecyclerView recyclerView;
    ImageView backbutton1_loadmore;
    subItemAdapter_More subItemAdapters;


    List<ProductSub> productSubs;
    TextView vendor_prov;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore);
        //
        initalizeWidget();
        vendor_prov=findViewById(R.id.vendor_prov);
        //
        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        session=intent.getStringExtra("session");
        categoryname=intent.getStringExtra("categoryname");
        jsonArray = intent.getStringExtra("jsonArray");
        l1=intent.getStringExtra("li");
        String str=categoryname.replaceAll("[^a-zA-Z]"," ");
        vendor_prov.setText(String.valueOf(str));
        productSubs=new ArrayList<>();
        System.out.println(l1+" "+categoryname);

        new AsyncFetch().execute();

        backbutton1_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    void assignAdaptere()
    {
        subItemAdapters=new subItemAdapter_More(loadmoreActivity.this,data,userid,session,categoryname);
        recyclerView.setAdapter(subItemAdapters);
        recyclerView.setLayoutManager(new GridLayoutManager(loadmoreActivity.this,2));
    }
    void initalizeWidget()
    {
        backbutton1_loadmore=findViewById(R.id.backbutton1_loadmore);
        recyclerView=findViewById(R.id.loadmore_products);
    }

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(loadmoreActivity.this);
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

                url = new URL("https://sellerportal.perfectmandi.com/load_moreProduct.php?id="+l1+"&cat="+categoryname);

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


            if(result.equalsIgnoreCase("unsuccessful"))
            {



            }
            else
            {
                try
                {
                    data=new JSONArray(result);
                    subItemAdapters=new subItemAdapter_More(loadmoreActivity.this,data,userid,session,categoryname);
                    recyclerView.setAdapter(subItemAdapters);
                    recyclerView.setLayoutManager(new GridLayoutManager(loadmoreActivity.this,2));

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }

    }

}