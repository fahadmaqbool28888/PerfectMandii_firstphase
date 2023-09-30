package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.getorder;
import com.vendor.perfectmandii.Model.vendor.dispatchitem;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterDispatch extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<getorder> data= Collections.emptyList();


    String category;


    String[] av;
    String setca;
    String session;
    Boolean flag;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterDispatch(Context context, List<getorder> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_dispatchitem, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        getorder current=data.get(position);



        myHolder.quantityNum.setText(current.order_quantity);
        myHolder.nameofitem.setText(current.Product_Name);
        Picasso.get().load(current.image_path).into(myHolder.ivFish);





    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {


        CheckBox check_dispatchItem;
       CircleImageView ivFish;
       TextView quantityNum,nameofitem;
        public MyHolder(View itemView)
        {
            super(itemView);
            nameofitem=itemView.findViewById(R.id.nameofitem);
            check_dispatchItem=itemView.findViewById(R.id.check_dispatchItem);
            ivFish= itemView.findViewById(R.id.ivFish_2);
            quantityNum=itemView.findViewById(R.id.quantityNum);

        }

    }


}