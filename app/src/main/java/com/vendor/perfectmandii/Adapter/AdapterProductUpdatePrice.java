package com.vendor.perfectmandii.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.PriceUpdateActivity;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.R;

import org.json.JSONException;
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

public class AdapterProductUpdatePrice extends RecyclerView.Adapter<AdapterProductUpdatePrice.MyHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<StockProductModel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    int count=0;
    private final boolean[] mCheckedStateA;
    String oldprice;
    String mewprice,id;
    SparseBooleanArray[] checkstatearray;

    int total,total1,value1;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterProductUpdatePrice(Context context, List<StockProductModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        mCheckedStateA = new boolean[data.size()];
        checkstatearray=new SparseBooleanArray[data.size()];
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.lis_rowv3, parent,false);
        MyHolder holder=new MyHolder(view);



        return holder;
    }

    public void reomve_item(int position)
    {
        data.remove(position);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        StockProductModel current=data.get(position);

        if (current.path.contains("https://sellerportal.perfectmandi.com/"))
        {
            Picasso.get().load(current.path).into(myHolder.produ_image);

        }
        else {
            String url="https://sellerportal.perfectmandi.com/"+current.path;
            Picasso.get().load(url).into(myHolder.produ_image);

        }


        myHolder.nameofitem.setText(current.name);
        myHolder.old_price_.setText(current.price);

        myHolder.new_price_.setText("");

        myHolder.btn_price_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               // String oldprice=myHolder.old_price_.getText().toString();
               mewprice=myHolder.new_price_.getText().toString();
               if (mewprice.equalsIgnoreCase("")||myHolder.new_price_.getText().equals(""))
               {
                   Toast.makeText(context,"Add Value",Toast.LENGTH_LONG).show();
               }else
               {
                   id=current.id;


                   Intent intent = new Intent("custom-message");




                   intent.putExtra("id",current.id);
                   intent.putExtra("price",oldprice);
                   intent.putExtra("newprice",mewprice);
                   intent.putExtra("pos",position);
                   intent.putExtra("flag","include");
                   LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
               }

            }
        });



        myHolder.ckick_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // String oldprice=myHolder.old_price_.getText().toString();
                mewprice=myHolder.new_price_.getText().toString();
                if (mewprice.equalsIgnoreCase("")||myHolder.new_price_.getText().equals(""))
                {
                    Toast.makeText(context,"Add Value",Toast.LENGTH_LONG).show();
                }else
                {
                    id=current.id;


                    Intent intent = new Intent("custom-message");




                    intent.putExtra("id",current.id);
                    intent.putExtra("price",oldprice);
                    intent.putExtra("newprice",mewprice);
                    intent.putExtra("pos",position);
                    intent.putExtra("flag","include");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });


        myHolder.ckick_button_u.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // String oldprice=myHolder.old_price_.getText().toString();
                mewprice=myHolder.new_price_.getText().toString();
                if (mewprice.equalsIgnoreCase("")||myHolder.new_price_.getText().equals(""))
                {
                    Toast.makeText(context,"Add Value",Toast.LENGTH_LONG).show();
                }else
                {
                    id=current.id;


                    Intent intent = new Intent("custom-message");




                    intent.putExtra("id",current.id);
                    intent.putExtra("price",oldprice);
                    intent.putExtra("newprice",mewprice);
                    intent.putExtra("pos",position);
                    intent.putExtra("flag","include");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });
    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {
        EditText  old_price_,new_price_;
        TextView stock_a,stock_e,miq_moq,nameofitem;
        ImageView produ_image;
        CheckBox checkBox;

        TextView btn_price_;
        CardView ckick_button;
        ConstraintLayout ckick_button_u;

        public MyHolder(View itemView) {
            super(itemView);
            stock_a=itemView.findViewById(R.id.stock_a);

            ckick_button=itemView.findViewById(R.id.ckick_button);
            ckick_button_u=itemView.findViewById(R.id.inside_v);
            btn_price_=itemView.findViewById(R.id.btn_price_);

            nameofitem=itemView.findViewById(R.id.nameofitem);
            produ_image=itemView.findViewById(R.id.ivFish_1);

          /*  checkBox=itemView.findViewById(R.id.item_Add);*/
            old_price_=itemView.findViewById(R.id.old_price_);
            new_price_=itemView.findViewById(R.id.new_price_);

        }

    }






}