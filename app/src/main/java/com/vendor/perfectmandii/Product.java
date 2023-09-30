package com.vendor.perfectmandii;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.vendor.perfectmandii.Adapter.AdapterProductPrime;

import com.vendor.perfectmandii.Model.CategoyByProductModel;

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


public class Product extends Fragment
{
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVFishPrice;
    private AdapterProductPrime mAdapter;
    ImageView imageView;
    int progress = 0;
    ProgressBar progressBar;
    String session,category,userid,urls,id;
    public Product(String session,String category,String userid,String id)
    {

        this.id=id;
        this.session=session;
        this.category=category;
        this.userid=userid;

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_product, container, false);
        imageView=rootview.findViewById(R.id.error_Res);
        mRVFishPrice=rootview.findViewById(R.id.product_perfect_mandi);


    /*    new AsyncFetch().execute();*/
        return rootview;
    }




    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog=new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog.setTitle("Perfect Mandi");
            progressDialog.setIcon(R.drawable.optimizedlogo);
            // progressDialog.setMessage("Data is Loading...");
            progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try {


                url = new URL("https://staginigserver.perfectmandi.com/productprime1.php");

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

            progressDialog.dismiss();
            System.out.println(result);

            if(result.equalsIgnoreCase("unsuccessful"))
            {
                Toast.makeText(getContext(),"Sorry,,Try Again",Toast.LENGTH_LONG).show();
                imageView.setImageResource(R.drawable.optimizedlogo);

            }
            else
            {
                List<CategoyByProductModel> data=new ArrayList<>();


                try {



                    JSONArray jArray = new JSONArray(result);



                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){


                        JSONObject json_data = jArray.getJSONObject(i);
                        CategoyByProductModel cpm = new CategoyByProductModel();
                        cpm.id=json_data.getString("id");
                        cpm.image_url= json_data.getString("image_url");
                        cpm.name=json_data.getString("name");
                        cpm.measure=json_data.getString("image_measure");
                        cpm.price=json_data.getString("image_price");

                        cpm.parent_Category=json_data.getString("image_category");

                        cpm.sku=json_data.getString("image_sku");

                        cpm.productprovider=json_data.getString("product_orovider");
                        cpm.productDescritpion=json_data.getString("Product_Description");

                        cpm.usersession=session;
                        cpm.usersessionid=userid;
                        data.add(cpm);
                    }

                    // Setup and Handover data to recyclerview

                    mAdapter = new AdapterProductPrime(getContext(), data);
                    mRVFishPrice.setAdapter(mAdapter);
                    mRVFishPrice.setLayoutManager(new GridLayoutManager(getContext(),3 ));

                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }

            }

        }

    }
}
