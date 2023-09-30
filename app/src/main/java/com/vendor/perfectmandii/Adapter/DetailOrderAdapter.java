package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.DetailOrderActivity;
import com.vendor.perfectmandii.DetailOrderModel;
import com.vendor.perfectmandii.Model.DailyOrder.orderDaily;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;



public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyHolder>
{
    private LayoutInflater inflater;
    List<DetailOrderModel> dailies= Collections.emptyList();
    private Context context;
    public DetailOrderAdapter(Context context,List<DetailOrderModel> dailies)
    {
            this.context=context;
            inflater=LayoutInflater.from(context);
            this.dailies=dailies;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.detail_product_order, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {


        MyHolder myHolder= (MyHolder) holder;
        DetailOrderModel current=dailies.get(position);
        myHolder.detailproduct_Name.setText(current.Product_Name);
        myHolder.detailproduct_Quantity.setText("QTY: "+current.order_quantity);
        System.out.println(current.Product_Name);

    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView detailproduct_Name,detailproduct_Quantity;
        MyHolder(View itView)
        {
            super(itView);
            detailproduct_Name=itView.findViewById(R.id.detailproduct_Name);
            detailproduct_Quantity=itView.findViewById(R.id.detailproduct_Quantity);
        }
    }
}

