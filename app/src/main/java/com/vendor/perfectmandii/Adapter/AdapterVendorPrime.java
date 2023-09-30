package com.vendor.perfectmandii.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.CategoryByVendorActivity;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.Vendor;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterVendorPrime extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<Vendor> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    String scategory;

    String[] av;
    String setca,pur;
    int id;
    String session,pcategory;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterVendorPrime(Context context, List<Vendor> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_productv22, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Vendor current=data.get(position);
        category=current.name;
        Picasso.get().load(current.image_url).into(myHolder.ivFish);
        myHolder.getstorename.setText(current.name);
        myHolder.getstorespeciality.setText(current.st_category);
//        myHolder.ratingBar.setRating(4);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    if(current.status.contentEquals("null"))
                    {
                        Toast.makeText(context,"This Category is not active",Toast.LENGTH_LONG).show();
                    }
                    else if(current.status.contentEquals("active"))
                    {
                        session=current.session;
                        pur=current.purchaserid;
                        setca=String.valueOf(current.id);
                        category=current.parent_category;


                        //Toast.makeText(context,category,Toast.LENGTH_LONG).show();

                      new AsyncFetch(setca,current.name,category).execute();
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


       CircleImageView ivFish;
       RatingBar ratingBar;
       CardView mainvps;

       TextView getstorename,getstorespeciality;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);


            ivFish=  (CircleImageView) itemView.findViewById(R.id.ivFish_vendorprime);
            getstorename=itemView.findViewById(R.id.getstorename);
            getstorespeciality=itemView.findViewById(R.id.getstorespeciality);

           // textFishName=itemView.findViewById(R.id.textname);

        }

    }



    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;
        String id,name,category;
        AsyncFetch(String id,String name,String category)
        {
            this.id=id;
            this.name=name;
            this.category=category;
        }

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
            try
            {
                url = new URL("https://staginigserver.perfectmandi.com/categoryvendor.php?id="+setca);
            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try
            {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);

            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK)
                {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
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
            pdLoading.dismiss();
            Intent intent=new Intent(context, CategoryByVendorActivity.class);
            intent.putExtra("value",result);
            intent.putExtra("va",id);
            intent.putExtra("name",name);
            intent.putExtra("userid",pur);
            intent.putExtra("session",session);
            intent.putExtra("categoy",category);

            context.startActivity(intent);


           // Toast.makeText(context,category,Toast.LENGTH_LONG).show();
        }

    }

}