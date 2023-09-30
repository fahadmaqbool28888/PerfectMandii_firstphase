package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.productprofilemodel;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class Adaptertestpart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<productprofilemodel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;

    String sbcategory;
    // create constructor to innitilize context and data sent from MainActivity
    public Adaptertestpart(Context context, List<productprofilemodel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
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
        productprofilemodel current=data.get(position);








        category=current.image_category;
        sbcategory=current.image_scategory;
        if(sbcategory.contentEquals("wood"))
        {
            Toast.makeText(context,sbcategory,Toast.LENGTH_LONG).show();
        }
        else if(sbcategory.contentEquals("plastic"))
        {
            Toast.makeText(context,sbcategory,Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,sbcategory,Toast.LENGTH_LONG).show();
        }
       // Picasso.get().load(R.drawable.ic_baseline_shopping_basket_24).into(myHolder.ivFish);
      // Picasso.get().load("http://staginigserver.perfectmandi.com/images/" + current.image_path).into(myHolder.ivFish);

        Picasso.get().load( current.image_path).into(myHolder.ivFish);



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