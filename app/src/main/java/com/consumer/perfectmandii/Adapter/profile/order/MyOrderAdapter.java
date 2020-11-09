package com.consumer.perfectmandii.Adapter.profile.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Model.modelProduct;
import com.consumer.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
public static final int CONNECTION_TIMEOUT = 10000;
public static final int READ_TIMEOUT = 15000;
private final Context context;
private final LayoutInflater inflater;
        List<modelProduct> data= Collections.emptyList();

        String category;



// create constructor to innitilize context and data sent from MainActivity
public MyOrderAdapter(Context context, List<modelProduct> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_myorder, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyHolder myHolder= (MyHolder) holder;
        modelProduct current=data.get(position);
        category=current.name;

        Picasso.get().load(R.drawable.plastic).into(myHolder.ivFish);
        // myHolder.textFishName.setText(current.name);


        myHolder.ratingBar.setRating(4);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v)
        {


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
                RatingBar ratingBar;


                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);

                    ivFish=  itemView.findViewById(R.id.ivFish);
                    ratingBar=itemView.findViewById(R.id.rating);
                    //   textFishName=itemView.findViewById(R.id.textname);


                }

            }

}