package com.vendor.perfectmandii.Adapter.ProductProperties;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.Activity.vendor.AddProductActivity;
import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.ProductProperties.price;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
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
import java.util.Collections;
import java.util.List;

public class priceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<price> data= Collections.emptyList();


    String category;


    // create constructor to innitilize context and data sent from MainActivity
    public priceAdapter(Context context, List<price> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.update_product_price_tray, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        price current=data.get(position);
        myHolder.item_name.setText(current.name);
        myHolder.serial_id.setText("#"+Integer.toString(position+1));
        myHolder.product_old_price.setText(current.currentprice);
  /*      category=current.servicename;
        //session=current.session;
        Picasso.get().load(current.serviceicon).into(myHolder.ivFish);*/


        myHolder.update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price=myHolder.product_old_price.getText().toString();
                Toast.makeText(context, price+"   " +"This price need to be updated", Toast.LENGTH_SHORT).show();

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

      TextView item_name,serial_id,update_button;
      EditText product_old_price,product_new_price;
      CardView update_button_board;
        public MyHolder(View itemView)
        {
            super(itemView);
            item_name= itemView.findViewById(R.id.item_name);
            serial_id=itemView.findViewById(R.id.serial_id);
            product_old_price=itemView.findViewById(R.id.product_old_price);
            product_new_price=itemView.findViewById(R.id.product_new_price);
            update_button_board=itemView.findViewById(R.id.update_button_board);
            update_button=itemView.findViewById(R.id.update_button);
        }

    }


}