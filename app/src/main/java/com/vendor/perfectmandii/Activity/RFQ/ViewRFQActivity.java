package com.vendor.perfectmandii.Activity.RFQ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.vendor.perfectmandii.Activity.RFQ.adapter.RFQItemAdapter;
import com.vendor.perfectmandii.Activity.RFQ.model.RFQItemModel;
import com.vendor.perfectmandii.Activity.StockUpdateActivity;
import com.vendor.perfectmandii.Adapter.AdapterProductAddtoitem;
import com.vendor.perfectmandii.DBHelper;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.Model.userVendor;
import com.vendor.perfectmandii.R;

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

public class ViewRFQActivity extends AppCompatActivity
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    RecyclerView recyclerView;
    RFQItemAdapter rfqItemAdapter;
    String vendor,storeid;
    DBHelper dbHelper;
    ArrayList<userVendor> pointModelsArrayList;
    void init()
    {
        pointModelsArrayList = dbHelper.readVendor();



        if (pointModelsArrayList.size()>0)
        {
            userVendor pointModel=pointModelsArrayList.get(0);
            vendor=pointModel.userid;
            storeid=pointModel.storeid;




        }
        else
        {


        }




    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rfqactivity);
        dbHelper=new DBHelper(ViewRFQActivity.this);
        recyclerView=findViewById(R.id.rfq_12);
        init();
        new AsyncFetch().execute();
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ViewRFQActivity.this);
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
                url = new URL("https://sellerportal.perfectmandi.com/view_rfqapi2.php?id="+storeid);

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
            List<RFQItemModel> data=new ArrayList<>();


            try
            {
                JSONArray jArray = new JSONArray(result);

                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);


                    /*[{"sr":1,"id":"12","date":"2023\/04\/11","purchaseInvoice":"90334518456837"}*/

                    RFQItemModel stockProductModel=new RFQItemModel();
                    stockProductModel.id=json_data.getString("id");
                    stockProductModel.date=json_data.getString("date");
                    stockProductModel.PO=json_data.getString("purchaseInvoice");
                    stockProductModel.items=json_data.getJSONArray("items");


                    JSONArray jsonArray=json_data.getJSONArray("items");
                    if (jsonArray.length()>0)
                    {
                        data.add(stockProductModel);
                    }



                    // fishData.usid=username;

                }
                LinearLayoutManager llm = new LinearLayoutManager(ViewRFQActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
               recyclerView.setLayoutManager(llm);
                rfqItemAdapter=new RFQItemAdapter(ViewRFQActivity.this,data);
                recyclerView.setAdapter(rfqItemAdapter);
               // recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                // Setup and Handover data to recyclerview



            } catch (Exception e) {
            }
        }

    }
}