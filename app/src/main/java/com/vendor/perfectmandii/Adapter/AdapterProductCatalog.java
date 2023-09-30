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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

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

public class AdapterProductCatalog extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    String scategory;
    AdaptersubProductCatalog mAdapter;
    RecyclerView mRVFishPrice;
    TextView textView;

    String cid,sessionid;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterProductCatalog(Context context, List<DataFish> data,RecyclerView mRVFishPrice,TextView textView){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.mRVFishPrice=mRVFishPrice;
        this.textView=textView;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_related_item, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataFish current=data.get(position);
        category=current.fishName;
        //Picasso.get().load(R.drawable.ic_baseline_shopping_basket_24).into(myHolder.ivFish);
        Picasso.get().load(current.fishImage).into(myHolder.ivFish);


        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              /*  Toast.makeText(context,category+current.usid,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, HomeActivity.class);
                intent.putExtra("value",current.usid);
                context.startActivity(intent);
*/

                if(sessionid==null)
                {
                    Intent intent=new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
                else
                {
                    mRVFishPrice.setVisibility(View.INVISIBLE);
                    cid=current.vendorprovider;
                    scategory=current.fishName;
                    new AsyncFetch().execute();
                    textView.setVisibility(View.VISIBLE);
                    System.out.println("https://staginigserver.perfectmandi.com/loadproductCfile.php?id=1 dollar & sid="+scategory+" & pid="+cid);


                }


            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textFishName;
        ImageView ivFish;
        TextView textSize;
        TextView textType;
        TextView textPrice;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            ivFish= (ImageView) itemView.findViewById(R.id.ivFish);

        }

    }



    private class AsyncFetch extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                /*                url = new URL(https://staginigserver.perfectmandi.com/loadproductCfile.php?id=1 dollar & sid="+scategory+" & pid="+id);
                 */
                url = new URL("https://staginigserver.perfectmandi.com/loadproductCfile.php?id=1 dollar & sid="+scategory+" & pid="+cid);
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
            //this method will be running on UI thread

            pdLoading.dismiss();
            mRVFishPrice.setVisibility(View.VISIBLE);


            List<DataFish> data=new ArrayList<>();

            pdLoading.dismiss();
            try {
                //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();


                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataFish fishData = new DataFish();
                    fishData.fishImage= json_data.getString("image_path");
                    fishData.fishName= json_data.getString("image_name");
                  /*  fishData.catName= json_data.getString("l2_product_name");
                    fishData.status=json_data.getString("status");*/
                    // fishData.usid=username;

                    fishData.vendorprovider=cid;
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview

                mAdapter = new AdaptersubProductCatalog(context, data);
                mRVFishPrice.setAdapter(mAdapter);
                mRVFishPrice.setLayoutManager( new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

            } catch (Exception e) {
                //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
            }




        }

    }





}