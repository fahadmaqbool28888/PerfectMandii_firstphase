package com.customer.perfectcustomer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.Authentication.LoginActivity;
import com.customer.perfectcustomer.Model.CategoyByProductModel;
import com.customer.perfectcustomer.Model.DataFish;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.ProductProfileActivity;
import com.customer.perfectcustomer.R;

import java.util.Collections;
import java.util.List;

public class AdapterProductPrime extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private final Context context;
    private final LayoutInflater inflater;
    public List<CategoyByProductModel> data= Collections.emptyList();

    String category;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterProductPrime(Context context, List<CategoyByProductModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_productv1, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyHolder myHolder= (MyHolder) holder;
        CategoyByProductModel current=data.get(position);

        myHolder.saleprice.setVisibility(View.INVISIBLE);




        category=current.name;
        Glide
                .with(context)
                .load(current.image_url)
                .centerCrop()
                .into(myHolder.ivFish);

      //  Picasso.get().load(current.image_url).into(myHolder.ivFish);
        myHolder.textFishName.setText(current.name);
        myHolder.productPrice.setText("PKR: "+current.price);


        myHolder.ratingBar.setRating(4);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if(current.usersession==null)
                {
                    Intent intent=new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
                else
                {

                    Intent intent=new Intent(context, ProductProfileActivity.class);
                    intent.putExtra("value",current.image_url);
                    intent.putExtra("id",current.productprovider);
                    intent.putExtra("proid",current.id);
                    intent.putExtra("userid",current.usersessionid);
                    intent.putExtra("session",current.usersession);
                    intent.putExtra("price",current.price);
                    intent.putExtra("category",current.parent_Category);
                    intent.putExtra("description",current.productDescritpion);
                    intent.putExtra("username",current.username);

                    intent.putExtra("vendor",current.productprovider);
                    intent.putExtra("name",current.name);
                    intent.putExtra("path",current.profilepic);
                    context.startActivity(intent);
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

        TextView textFishName,productPrice,saleprice;
        ImageView ivFish;
        RatingBar ratingBar;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            ivFish=  itemView.findViewById(R.id.ivFish);
            ratingBar=itemView.findViewById(R.id.rating);
            textFishName=itemView.findViewById(R.id.ppname);
            productPrice=itemView.findViewById(R.id.pprice);
            saleprice=itemView.findViewById(R.id.sprice);


        }

    }





}