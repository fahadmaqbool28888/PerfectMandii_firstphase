package com.vendor.perfectmandii.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.ProductCatalog;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    String scategory;

    String[] av;
    String setca;
    String session;
    public AdapterFish(Context context, List<DataFish> data)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_fish, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataFish current=data.get(position);
        category=current.fishName;
        session=current.session;
        Picasso.get().load(current.fishImage).into(myHolder.ivFish);
       // myHolder.textFishName.setText(current.fishName);


        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(current.status=="active")
                {
                    if (current.session==null)
                    {
                        Intent intent=new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(context,DashboardActivity.class);
                        intent.putExtra("name",current.fishName);
                        intent.putExtra("session",current.session);
                        intent.putExtra("id",current.usid);
                        intent.putExtra("userid",current.userid);
                        context.startActivity(intent);



                        Toast.makeText(context,current.fishName+session,Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context,"This category is not active",Toast.LENGTH_LONG).show();
                }





            }
        });

    }




    // return total item from List
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

        TextView textFishName;
       ImageView ivFish;
        public MyHolder(View itemView)
        {
            super(itemView);
            ivFish= itemView.findViewById(R.id.ivFish);
           // textFishName=itemView.findViewById(R.id.textname);
        }

    }



    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
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
                url = new URL("https://staginigserver.perfectmandi.com/l3pcategory.php?id="+setca);

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




            pdLoading.dismiss();
           // Toast.makeText(context,result,Toast.LENGTH_LONG).show();

            Intent intent=new Intent(context, ProductCatalog.class);
            intent.putExtra("value",result);
            intent.putExtra("val",setca);
            context.startActivity(intent);







        }

    }

}