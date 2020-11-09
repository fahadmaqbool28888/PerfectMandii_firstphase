package com.consumer.perfectmandii.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.Model.OrderModel.ordermodel;
import com.consumer.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderConfirmation extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private final Context context;
    private final LayoutInflater inflater;
    List<ordermodel> data= Collections.emptyList();
    ordermodel current;
    int currentPos=0;
    String category;
    String scategory;

    String[] av;
    String setca,pur;
    int id;
    String session;

    // create constructor to innitilize context and data sent from MainActivity
    public OrderConfirmation(Context context, List<ordermodel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_orderd, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        ordermodel current = data.get(position);

        Picasso.get().load(current.image_path).into(myHolder.ivFish);
        myHolder.ratingBar.setRating(4);
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


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            mainvps=(CardView)itemView.findViewById(R.id.mainsae);
            ivFish=  (CircleImageView) itemView.findViewById(R.id.ivFish);
            ratingBar=itemView.findViewById(R.id.rating);
           // textFishName=itemView.findViewById(R.id.textname);

        }

    }





}