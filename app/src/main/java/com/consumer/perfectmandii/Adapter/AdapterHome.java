package com.consumer.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.DashboardActivity;
import com.consumer.perfectmandii.LoginActivity;
import com.consumer.perfectmandii.Model.DataFish;
import com.consumer.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private final Context context;
    private final LayoutInflater inflater;
    List<DataFish> data= Collections.emptyList();


    String category;


    String[] av;
    String setca;
    String session;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterHome(Context context, List<DataFish> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_homepr, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        DataFish current=data.get(position);
        category=current.fishName;
        session=current.session;
        Picasso.get().load(current.fishImage).into(myHolder.ivFish);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(current.status.equalsIgnoreCase("active"))
                {
                    if (current.session==null)
                    {
                        Intent intent=new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(context,DashboardActivity.class);
                        intent.putExtra("cname",current.fishName);
                        intent.putExtra("session",current.session);
                        intent.putExtra("id",current.usid);
                        intent.putExtra("userid",current.userid);
                        intent.putExtra("orderid",current.orderid);
                        intent.putExtra("name",current.username);
                        intent.putExtra("profilepic",current.profilepic);
                        context.startActivity(intent);

                    }
                }
                else
                {
                    Toast.makeText(context,"Not Active",Toast.LENGTH_LONG).show();
                }


            }
        });
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