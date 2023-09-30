package com.vendor.perfectmandii.Activity.RFQ.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Activity.RFQ.DetailRFQActivity;
import com.vendor.perfectmandii.Activity.RFQ.model.RFQItemModel;
import com.vendor.perfectmandii.R;

import java.util.List;

public class RFQItemAdapter extends RecyclerView.Adapter<RFQItemAdapter.RFQItemViewHolder>
{

    Context context;
    List<RFQItemModel> rfqItemModels;
    LayoutInflater layoutInflater;

    public RFQItemAdapter(Context context, List<RFQItemModel> rfqItemModels)
    {
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        this.rfqItemModels = rfqItemModels;
    }

    @Override
    public RFQItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.activity_viewrfq,parent,false);
        return new RFQItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RFQItemViewHolder holder, int position)
    {
        System.out.println(rfqItemModels.toString());


        RFQItemModel rfqItemModel=rfqItemModels.get(position);

        //  System.out.println(rfqItemModel.sr);

    //    holder.Num.setText(rfqItemModel.sr);
        holder.date_rfqgen.setText(rfqItemModel.date);
        holder.num_rfqgen.setText(rfqItemModel.PO);
        holder.btn_rfqgen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, DetailRFQActivity.class);
                intent.putExtra("jsonArray", rfqItemModel.items.toString());

                context.startActivity(intent);

            }
        });




    }

    @Override
    public int getItemCount() {
        return rfqItemModels.size();
    }

    public class RFQItemViewHolder extends RecyclerView.ViewHolder{

        TextView Num,date_rfqgen,num_rfqgen;
        Button btn_rfqgen;

        public RFQItemViewHolder(View itemView) {
            super(itemView);
            Num=itemView.findViewById(R.id.Num);
            date_rfqgen=itemView.findViewById(R.id.date_rfqgen);
            num_rfqgen=itemView.findViewById(R.id.num_rfqgen);
            btn_rfqgen=itemView.findViewById(R.id.btn_rfqgen);
        }
    }
}
