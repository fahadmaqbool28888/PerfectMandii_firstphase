package com.consumer.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.consumer.perfectmandii.Adapter.DataContentAdapter_FlashSale;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;

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

public class FlashSaleActivity extends AppCompatActivity
{

    DataContentAdapter_FlashSale_More dataContentAdapter_flashSale;
    RecyclerView flashsale_more;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_sale);
        flashsale_more=findViewById(R.id.flashsale_more);
        new AsyncFetch().execute();

    }
    @Override
    protected void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog=new ProgressDialog(FlashSaleActivity.this);
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



                url = new URL("https://sellerportal.perfectmandi.com/flashsaleproduct.php");

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
                Toast.makeText(FlashSaleActivity.this,"Sorry,,Try Again",Toast.LENGTH_LONG).show();
                //imageView.setImageResource(R.drawable.optimizedlogo);

            }
            else
            {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    dataContentAdapter_flashSale=new DataContentAdapter_FlashSale_More(FlashSaleActivity.this,jsonArray);
                    flashsale_more.setAdapter(dataContentAdapter_flashSale);
                    flashsale_more.setLayoutManager(new GridLayoutManager(FlashSaleActivity.this, 2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }

    }
}