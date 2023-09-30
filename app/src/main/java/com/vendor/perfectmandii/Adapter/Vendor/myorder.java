package com.vendor.perfectmandii.Adapter.Vendor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.vendor.VendorConfirmoRDER;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class myorder extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<VendorConfirmoRDER> data= Collections.emptyList();


    String category;


    String[] av;
    String setca;
    String session;
    // create constructor to innitilize context and data sent from MainActivity
    public myorder(Context context, List<VendorConfirmoRDER> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_ordertray, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
       MyHolder myHolder= (MyHolder) holder;
        VendorConfirmoRDER current=data.get(position);
      /*  category=current.servicename;
        //session=current.session;
        Picasso.get().load(current.serviceicon).into(myHolder.ivFish);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(current.servicestatus.equalsIgnoreCase("active"))
                {
                    Intent intent=new Intent(context, DashboardActivity.class);
                    context.startActivity(intent);
                 *//*   if (current.session==null)
                    {
                        Intent intent=new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(context,DashboardActivity.class);
                        intent.putExtra("name",current.fishName);
                        intent.putExtra("session",current.session);
                        intent.putExtra("id",current.usid);
                        intent.putExtra("userid",current.userid);
                        intent.putExtra("orderid",current.orderid);
                        context.startActivity(intent);
                        Toast.makeText(context,current.fishName+session,Toast.LENGTH_LONG).show();
                    }*//*
                    Toast.makeText(context,"Active",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context,"Not Active",Toast.LENGTH_LONG).show();
                }
*/

       /*     }
        });*/
    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

        TextView textFishName;
        ImageView ivFish;
        public MyHolder(View itemView)
        {
            super(itemView);
            ivFish= itemView.findViewById(R.id.ivFish);

        }

    }


}
