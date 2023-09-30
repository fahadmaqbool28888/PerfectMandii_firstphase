package com.vendor.perfectmandii.CategoryDetail.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class bulkupdate extends AsyncTask<String,String,String>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    HttpURLConnection conn;
    URL url = null;
    String urls="https://sellerportal.perfectmandi.com/update_bulk.php";
    Context context;
    ProgressBar progressBar;
    String s1,s2,s3,id,price;
    public bulkupdate(Context context, ProgressBar progressBar, String id, String s1, String s2, String s3,String price) {
        this.context = context;
        this.progressBar = progressBar;
        this.s1=s1;
        this.s2=s2;
        this.s3=s3;
        this.id=id;
        this.price=price;
    }



    protected abstract void passdata(String str);


    public bulkupdate()
    {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(String... strings) {
        try
        {
            System.out.println(urls+"/?id="+id+"&s1="+s1+"&s2="+s2+"&s3="+s3+"&price="+price);
            url = new URL(urls+"/?id="+id+"&s1="+s1+"&s2="+s2+"&s3="+s3+"&price="+price);
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        passdata(s);

        Toast.makeText(context,s,Toast.LENGTH_LONG).show();

    }
}
