package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Activity.DailyOrder.DailyOrderActivity;
import com.vendor.perfectmandii.Activity.DailyOrder.DetailOfOrderActivity;
import com.vendor.perfectmandii.DetailOrderActivity;
import com.vendor.perfectmandii.Model.CategoyByvendorModel;
import com.vendor.perfectmandii.Model.DailyOrder.orderDaily;
import com.vendor.perfectmandii.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyOrderAdapter extends RecyclerView.Adapter<DailyOrderAdapter.MyHolder>
{
    private LayoutInflater inflater;
    List<orderDaily> dailies= Collections.emptyList();
    private Context context;
    public DailyOrderAdapter(Context context,List<orderDaily> dailies)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.dailies=dailies;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.order_log, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( MyHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        orderDaily current=dailies.get(position);

        holder.order_id_log.setText(current.customer_order);
        holder.order_on_log.setText(current.order_place);
        holder.order_del_log.setText(current.Tentative_Date);

        holder.order_view_log.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, DetailOfOrderActivity.class);
                intent.putExtra("order",current.purchaseInvoice);
                intent.putExtra("data",current.jsonArray.toString());
                intent.putExtra("data1",current.deliver_Address.toString());
                intent.putExtra("data2",current.order_Description.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dailies.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView order_id_log,order_on_log,order_del_log,order_view_log;
        public MyHolder(View itemView)
        {
            super(itemView);
            order_id_log=itemView.findViewById(R.id.order_id_log);
            order_on_log=itemView.findViewById(R.id.order_on_log);
            order_del_log=itemView.findViewById(R.id.order_del_log);
            order_view_log=itemView.findViewById(R.id.order_view_log);

        }
    }
}
