package com.mandi.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.mandi.perfectmandii.Model.DataFish;
import com.mandi.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterFish extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterFish(Context context, List<DataFish> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_fish, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataFish current=data.get(position);




        // load image into imageview using glide
/*        Glide.with(context).load("http://staginigserver.perfectmandi.com/images/" + current.fishImage)
                .into(myHolder.ivFish);*/
        category=current.fishName;
        Picasso.get().load(R.drawable.ic_baseline_shopping_basket_24).into(myHolder.ivFish);
      //  Picasso.get().load("http://staginigserver.perfectmandi.com/images/" + current.fishImage).into(myHolder.ivFish);


        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
           /*     Toast.makeText(context,category+current.usid,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context, detailProductActivity.class);
                intent.putExtra("value",current.usid);
                context.startActivity(intent);*/



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

}