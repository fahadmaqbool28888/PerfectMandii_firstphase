package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.LoginActivity;
import com.vendor.perfectmandii.Model.CategoyByProductModel;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.ProductProfileActivity;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterProductPrime extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    public List<CategoyByProductModel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    String scategory;

    String[] av;
    String setca;
    int id;
    String session;
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





        category=current.name;

        Picasso.get().load(current.image_url).into(myHolder.ivFish);
        myHolder.textFishName.setText(current.name);
        myHolder.productPrice.setText(current.price);


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

                    intent.putExtra("vendor",current.productprovider);
                    intent.putExtra("name",current.name);
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

        TextView textFishName,productPrice;
        ImageView ivFish;
        RatingBar ratingBar;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            ivFish=  itemView.findViewById(R.id.ivFish);
            ratingBar=itemView.findViewById(R.id.rating);
            textFishName=itemView.findViewById(R.id.ppname);
            productPrice=itemView.findViewById(R.id.pprice);


        }

    }





}