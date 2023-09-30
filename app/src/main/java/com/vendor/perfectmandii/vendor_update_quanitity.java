package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.vendor.perfectmandii.Adapter.ProductProperties.priceAdapter;
import com.vendor.perfectmandii.Adapter.ProductProperties.quantityAdapter;
import com.vendor.perfectmandii.Model.ProductProperties.price;

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

public class vendor_update_quanitity extends AppCompatActivity
{
    RecyclerView recyclerView;
    quantityAdapter quantityadapter;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_update_quanitity);
        intializeWidget();
        new AsyncFetch().execute();
    }

    void intializeWidget()
    {
        recyclerView=findViewById(R.id.productList_);

    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;
        String apiUrl = "https://staginigserver.perfectmandi.com/get_product_detail.php";
        ProgressDialog progressDialog=new ProgressDialog(vendor_update_quanitity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            progressDialog = new ProgressDialog(vendor_update_quanitity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();



        }
        @Override
        protected String doInBackground(String... params) {
            try
            {
                url = new URL(apiUrl);
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


     /*       // implement API in background and store the response in current variable
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);

                    urlConnection = (HttpURLConnection) url
                            .openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        System.out.print(current);

                    }
                    // return the data to onPostExecute method
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;*/
        }


        @Override
        protected void onPostExecute(String result)
        {

            progressDialog.dismiss();
            Toast.makeText(vendor_update_quanitity.this, result, Toast.LENGTH_SHORT).show();
            List<price> productproperties=new ArrayList<>();

            try {



                JSONArray jArray = new JSONArray(result);



                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    price p_price=new price();
                    //
                    //  "name":"Glass Plain Mt","description":"1 dozen glass plain magnet","price":"540"
                    p_price.name= json_data.getString("name");
                    p_price.description= json_data.getString("description");
                    p_price.currentquantity= json_data.getString("quantity");
                    p_price.productcode=json_data.getString("sku");


                    productproperties.add(p_price);
                }

                // Setup and Handover data to recyclerview

                quantityadapter = new quantityAdapter(vendor_update_quanitity.this, productproperties);
                progressDialog.dismiss();
                recyclerView.setAdapter(quantityadapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(vendor_update_quanitity.this));


            } catch (Exception e)
            {

            }



        }

    }
}