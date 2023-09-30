package com.vendor.perfectmandii.Adapter.DashboardAdapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.EditProductActivity;
import com.vendor.perfectmandii.ManageShelf_Activity;
import com.vendor.perfectmandii.Model.ProductShelf;
import com.vendor.perfectmandii.Model.dashboardModel.modelDashboard;
import com.vendor.perfectmandii.R;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductShelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    String did;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<ProductShelf> data= Collections.emptyList();

    AlertDialog.Builder builder;


    public ProductShelfAdapter(Context context, List<ProductShelf> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        builder = new AlertDialog.Builder(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.layout_productshelf, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        ProductShelf current=data.get(position);
       // System.out.println("this is :"+current.store_listing);

      /*  if(current.store_listing.equalsIgnoreCase("1"))
        {
            myHolder.review.setVisibility(View.GONE);
            myHolder.cont_trigger.setVisibility(View.VISIBLE);
            myHolder.aSwitch.setChecked(true);
        }*/

        if(current.lisiting_status.equalsIgnoreCase("active"))
        {
            myHolder.review.setVisibility(View.GONE);
            myHolder.cont_trigger.setVisibility(View.VISIBLE);

        }
        else
        {

            myHolder.review.setVisibility(View.GONE);
            myHolder.cont_trigger.setVisibility(View.VISIBLE);
            //myHolder.aSwitch.setChecked(false);

        }



       /* myHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    boolean state=data.get(position).isChecked();
                    data.get(position).setChecked(!state);
                    new AsyncFetch(current.userid, current.id, "enable",position).execute();
                }
                else
                {
                    new AsyncFetch(current.userid, current.id, "disable",position).execute();
                }
            }
        });*/



        myHolder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean state=data.get(position).isChecked();

                data.get(position).setChecked(!state);
                if (!state)
                {
                    new AsyncFetch(current.userid, current.id, "enable",position).execute();

                }
                else
                {
                    new AsyncFetch(current.userid, current.id, "disable",position).execute();
                }

            }
        });

        myHolder.aSwitch.setChecked(data.get(position).isChecked());

        //holder.EventSwitch.setChecked(userListResponseData.get(position).isChecked());
        myHolder.pname_shelf.setText(current.Product_Name);
/*
        if (current.image_path.contains("https://sellerportal.perfectmandi.com/"))
        {
            Glide.with(context).load(current.image_path).into(myHolder.pimg_shelf);

        }
        else {
            String urel="https://sellerportal.perfectmandi.com/"+current.image_path;
            System.out.println(urel);
            Glide.with(context).load(urel).into(myHolder.pimg_shelf);


        }*/


        if (current.image_path.contains("https://sellerportal.perfectmandi.com/"))
        {
            Picasso.get().load(current.image_path).into(myHolder.pimg_shelf);

        }
        else {
            String url="https://sellerportal.perfectmandi.com/"+current.image_path;
            Picasso.get().load(url).into(myHolder.pimg_shelf);

        }
      //  System.out.println(current.image_path);


        myHolder.edit_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context,EditProductActivity.class);
                intent.putExtra("id",current.id);
                intent.putExtra("name",current.Product_Name);
                intent.putExtra("desc",current.Product_Description);
                intent.putExtra("price",current.Product_Unit_Price);
                intent.putExtra("quan",current.Product_Unit_Quan);
                intent.putExtra("moq",current.MOQ);
                intent.putExtra("img",current.image_path);
                intent.putExtra("pro",current.provider);
                context.startActivity(intent);
            }
        });
        myHolder.delete_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                builder.setIcon(R.drawable.optimizedlogo);
                builder.setTitle("Perfect Mandi");

                //Setting message manually and performing action on button click
                builder.setMessage("Are you sure you want to delete this product ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                did=current.id;
                                new Delete_Fetch(position).execute();
                                Toast.makeText(context,did,
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(context,"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("PerfectMANDI");
                alert.show();

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

        TextView pname_shelf,dvalue,review;
        ImageView pimg_shelf,edit_option,delete_option;
        LinearLayout cont_trigger;

        Switch aSwitch;

        CircleImageView ivFish;
        public MyHolder(View itemView)
        {
            super(itemView);
            cont_trigger=itemView.findViewById(R.id.cont_trigger);
            pname_shelf= itemView.findViewById(R.id.pname_shelf);
            pimg_shelf=itemView.findViewById(R.id.pimg_shelf);
            aSwitch=itemView.findViewById(R.id.switch1);
            review=itemView.findViewById(R.id.review_product);

            edit_option=itemView.findViewById(R.id.edit_option);
            delete_option=itemView.findViewById(R.id.delete_option);

        }

    }


        private class AsyncFetch extends AsyncTask<String, String, String> {
            String userid, productcode, flag;
            int pos;

            public static final int CONNECTION_TIMEOUT = 10000;
            public static final int READ_TIMEOUT = 15000;
            // ProgressDialog pdLoading = new ProgressDialog(getContext());
            HttpURLConnection conn;
            URL url = null;

            AsyncFetch(String userid, String productcode, String flag, int pos) {
                this.userid = userid;
                this.productcode = productcode;
                this.flag = flag;
                this.pos = pos;
            }


            ProgressDialog progressDialog = new ProgressDialog(context);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();


            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    url = new URL("https://sellerportal.perfectmandi.com/set_shelf.php?id=" + userid + "&pc=" + productcode + "&flag=" + flag + "&pos=");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return e.toString();
                }
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("GET");

                    // setDoOutput to true as we recieve data from json file
                    conn.setDoOutput(true);

                } catch (IOException e1) {
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


                progressDialog.dismiss();
                System.out.println(result);

                if(result.equalsIgnoreCase(""))
                {

                }
                else
                {
                    Toast.makeText(context,"Data Recieve",Toast.LENGTH_LONG).show();
                    System.out.println(result);
                }





            }

        }

    private class Delete_Fetch extends AsyncTask<String, String, String> {
        String userid, productcode, flag;
        int pos;

        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;
        // ProgressDialog pdLoading = new ProgressDialog(getContext());
        HttpURLConnection conn;
        URL url = null;


        public Delete_Fetch(int pos)
        {
            this.pos=pos;
        }


        ProgressDialog progressDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("https://sellerportal.perfectmandi.com/Delete_VProduct.php?id="+did);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
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


            progressDialog.dismiss();
            System.out.println(result);

            if(result.equalsIgnoreCase(""))
            {

            }
            else
            {
                Toast.makeText(context,"Record updated successfully",Toast.LENGTH_LONG).show();
                System.out.println(result);
              removeAt(pos);

            }





        }


        public void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        }

    }



}