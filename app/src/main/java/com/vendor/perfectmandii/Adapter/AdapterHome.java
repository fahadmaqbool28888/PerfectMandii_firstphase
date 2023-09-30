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

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Activity.vendor.AddProductActivity;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.MainActivity;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.profile_Updates.ProductInformationActivity;

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
import java.util.Collections;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<vendorServiceModel> data= Collections.emptyList();


    String category;

    String usernumber;

    String[] av;
    String setca;
    String session;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterHome(Context context, List<vendorServiceModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_homepr, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        vendorServiceModel current=data.get(position);
        category=current.servicename;
        //session=current.session;
        Picasso.get().load(current.serviceicon).into(myHolder.ivFish);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(current.servicestatus.equalsIgnoreCase("active"))
                {

                    if(current.servicename.equalsIgnoreCase("view order"))
                    {
                        Intent intent=new Intent(context,DashboardActivity.class);
                        intent.putExtra("userid",current.usernumber);
                        context.startActivity(intent);
                    }
                    else if (current.servicename.equalsIgnoreCase("add product"))
                    {


                        usernumber=current.userid;

                        new AsyncFetch().execute();

                    /*    Intent intent=new Intent(context, AddProductActivity.class);
                        intent.putExtra("userid",current.usernumber);
                        context.startActivity(intent);*/
                    }
                    else if (current.servicename.equalsIgnoreCase("add store"))
                    {
                        Intent intent=new Intent(context, AddStoreActivity.class);
                        intent.putExtra("userid",current.userid);
                        intent.putExtra("name",current.name);
                        intent.putExtra("session",session);
                        context.startActivity(intent);
                    }


                 /*   if (current.session==null)
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
                        intent.putExtra("orderid",current.orderid);
                        context.startActivity(intent);
                        Toast.makeText(context,current.fishName+session,Toast.LENGTH_LONG).show();
                    }*/
                    Toast.makeText(context,"Active",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context,"Not Active",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
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

        }

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        ProgressDialog progressDialog=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  progressDialog.show();



        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                System.out.println("https://staginigserver.perfectmandi.com/storeapprove.php?id="+usernumber);
                url = new URL("https://staginigserver.perfectmandi.com/storeapprove.php?id="+usernumber);
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
        protected void onPostExecute(String result)
        {
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();

            String id ,name,image_url,status,resp=null;
            if ("Account in approval stages".equalsIgnoreCase(result))
            {
                //progressDialog.dismiss();
                Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
            }
            else
            {


                try {



                    JSONArray jArray = new JSONArray(result);



                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        /* "id": "8",
                            "name": "Fahad theng",
                            "image_url": "https://staginigserver.perfectmandi.com/images/storeicon/7.png",
                            "status": "active",
                            "resp": "Account approved"*/
                        resp=json_data.getString("resp");
                        status=json_data.getString("status");
                        image_url=json_data.getString("image_url");
                        name=json_data.getString("name");
                        id=json_data.getString("id");
                        Intent intent=new Intent(context, ProductInformationActivity.class);
                       // Intent intent=new Intent(context, AddProductActivity.class);
                        intent.putExtra("userid",usernumber);
                        intent.putExtra("session",session);
                        intent.putExtra("id",id);
                        context.startActivity(intent);

                    }



                    // Setup and Handover data to recyclerview


                } catch (Exception e)
                {

                }








            }

        }

    }
}