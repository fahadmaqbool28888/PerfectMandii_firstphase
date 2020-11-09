package com.consumer.perfectmandii.Controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Adapter.ContentAdapter;
import com.consumer.perfectmandii.Model.ContentModel;
import com.consumer.perfectmandii.R;

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

public class ContentController extends AsyncTask<String, String, String>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    HttpURLConnection conn;
    URL url = null;
    Context context;String urls;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    ImageView imageView;
    ContentAdapter contentAdapter;
    List<ContentModel> data;
    public ContentController(RecyclerView recyclerView,Context context,String urls)
    {
        this.recyclerView=recyclerView;
        this.context=context;
        this.urls=urls;
        data=new ArrayList<>();

    }
    @Override
    protected String doInBackground(String... strings) {

        try {


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
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Perfect Mandi");
        progressDialog.setIcon(R.drawable.optimizedlogo);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();


        ContentModel contentModel=new ContentModel();

        try {



            JSONArray jArray = new JSONArray(s);

            // Extract data from json and store into ArrayList as class objects
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);


                contentModel.head_name=json_data.getString("di_hname");
                //contentModel.catalog=json_data.getJSONArray("di_catalog");

                System.out.println(contentModel.head_name);
                System.out.println(contentModel.catalog.toString());
              data.add(contentModel);
            }

/*            contentAdapter=new ContentAdapter(context,data);

            recyclerView.setAdapter(contentAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));*/

            // Setup and Handover data to recyclerview


        } catch (Exception e) {
            //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void assignadapter(List<ContentModel> datas)
    {


    }
}
