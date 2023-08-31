package com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.Home.Fragment.ProductBy.Category.by;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Adapter.AdapterVendorPrime;
import com.customer.perfectcustomer.Model.Vendor;
import com.customer.perfectcustomer.R;

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


public class vendor extends Fragment
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    AdapterVendorPrime mAdapter;
    int progress = 0;
    ProgressBar progressBar;
    String session,id,userid,categoryname;
    public vendor(String categoryname)
    {

  /*      this.session=session;
        this.id=id;
        this.userid=userid;*/
        this.categoryname=categoryname;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_vendor, container, false);


        mRVFishPrice=rootview.findViewById(R.id.vendor_perfect_mandi);


        new AsyncFetch().execute();



        return rootview;
    }


    private class AsyncFetch extends AsyncTask<String, String, String>
    {
         ProgressDialog progressDialog = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            progressDialog.setMessage("Data is Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params)
        {
            try {
                String urls="https://sellerportal.perfectmandi.com/vendorprime.php?id="+categoryname;

                System.out.println(urls);
                url = new URL(urls);

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

            System.out.println("This is "+result);
            //Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
            List<Vendor> data=new ArrayList<>();

            try {
                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    Vendor fishData = new Vendor();
                    fishData.image_url= json_data.getString("image_url");
                    fishData.name= json_data.getString("PMstore_Name");
                   // fishData.status=json_data.getString("status");
                    fishData.id=json_data.getInt("store_id");
                    fishData.st_category=json_data.getString("category");
                    fishData.parent_category=categoryname;
                   /* fishData.session=session;
                    fishData.purchaserid=userid;*/


                    data.add(fishData);
                }



                mAdapter = new AdapterVendorPrime(getContext(), data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

            } catch (Exception e) {

            }
        }

    }
}