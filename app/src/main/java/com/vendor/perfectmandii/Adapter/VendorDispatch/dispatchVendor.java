package com.vendor.perfectmandii.Adapter.VendorDispatch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Activity.Dispatch.TransitDispatchActivity;
import com.vendor.perfectmandii.Model.vendorDispatch.vendorDispatchModel;
import com.vendor.perfectmandii.R;

import java.util.List;

public class dispatchVendor extends RecyclerView.Adapter<dispatchVendor.MyHolder>
{
    Context context;
    LayoutInflater inflater;
    List<vendorDispatchModel> data;

    public dispatchVendor(Context context,List<vendorDispatchModel> data)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View wiView=inflater.inflate(R.layout.layoutvendordispatch,parent,false);
       MyHolder viewMyHolder=new MyHolder(wiView);
        return viewMyHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {


        MyHolder myHolder=(MyHolder) holder;
        vendorDispatchModel model=data.get(position);
        holder.orderidtransit.setText(model.orderid);

        holder.ordertransit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inb=new Intent(context, TransitDispatchActivity.class);
                inb.putExtra("pi",model.orderid);
                context.startActivity(inb);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView orderidtransit,ordertransit;
        public MyHolder(View itView)
        {
        super(itView);
        orderidtransit=itView.findViewById(R.id.orderidtransit);
        ordertransit=itView.findViewById(R.id.ordertransit);
        }
    }
}
