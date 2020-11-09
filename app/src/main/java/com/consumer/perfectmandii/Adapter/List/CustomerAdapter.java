package com.consumer.perfectmandii.Adapter.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Model.CategoyByProductModel;
import com.consumer.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class CustomerAdapter extends BaseAdapter
{

    ImageView ivFish; TextView textFishName,productPrice;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private final Context context;
    private final LayoutInflater inflater;
    List<CategoyByProductModel> data= Collections.emptyList();


    String category;


    String[] av;
    String setca;
    String session;
    public CustomerAdapter(Context context,List<CategoyByProductModel> data)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;

    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.container_productv1, null);
        CategoyByProductModel current=data.get(position);
        ivFish= convertView.findViewById(R.id.ivFish);
        textFishName=convertView.findViewById(R.id.ppname);
        productPrice=convertView.findViewById(R.id.pprice);

      //  Picasso.get().load(current.image_url).into(ivFish);
      /*  Glide
                .with(context)
                .load(current.image_url)
                .centerCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(myImageView);*/
        textFishName.setText(current.name);
        productPrice.setText(current.price);
        Glide
                .with(context)
                .load(current.image_url)
                .centerCrop()
                .into(ivFish);
        return convertView;
    }
}
