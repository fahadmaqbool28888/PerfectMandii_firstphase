package com.vendor.perfectmandii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Adapter.DailyOrderAdapter;
import com.vendor.perfectmandii.Adapter.DetailOrderAdapter;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyHolder>
{

    LayoutInflater inflater;
    List<DetailOrderModel> detailOrder;
    Context context;

    public OrderDetailAdapter(Context context,List<DetailOrderModel> detailOrder)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.detailOrder=detailOrder;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.order_log, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        MyHolder myHolder=(MyHolder) holder;

   /*     System.out.println(String.valueOf(position));
        DetailOrderModel detailOrderModel=detailOrder.get(position);

        System.out.println(detailOrderModel.Product_Name);
        holder.detailproduct_Name.setText(detailOrderModel.Product_Name);*/
        //  holder.detailproduct_Name.setText(detailOrderModel.Product_Name);
    }

    @Override
    public int getItemCount() {
        return detailOrder.size()+2;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
       //TextView detailproduct_Name;
       public MyHolder(View view)
        {
            super(view);
           // detailproduct_Name=view.findViewById(R.id.detailproduct_Name);

        }
    }
}
