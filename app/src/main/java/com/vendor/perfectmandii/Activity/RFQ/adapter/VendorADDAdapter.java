package com.vendor.perfectmandii.Activity.RFQ.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Activity.RFQ.model.DetailRFQ;
import com.vendor.perfectmandii.R;

import java.util.List;

public abstract class VendorADDAdapter extends RecyclerView.Adapter<VendorADDAdapter.RFQItemViewHolder>
{

    Context context;
    List<DetailRFQ> rfqItemModels;
    LayoutInflater layoutInflater;

    public VendorADDAdapter(Context context, List<DetailRFQ> rfqItemModels)
    {
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        this.rfqItemModels = rfqItemModels;
    }

    @Override
    public RFQItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_rfqaddproduct,parent,false);
        return new RFQItemViewHolder(view);
    }

    public abstract void sendData(DetailRFQ detailRFQ,int position);

    @Override
    public void onBindViewHolder(RFQItemViewHolder holder, int position)
    {


        DetailRFQ rfqItemModel=rfqItemModels.get(position);

        Glide.with(context).load(rfqItemModel.img).into(holder.imageView);

        holder.rfqstock_.setText(rfqItemModel.stock);
        holder.tagprice_.setText(rfqItemModel.price);
        holder.rfqitem.setText(rfqItemModel.name);

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                rfqItemModel.getstock=holder.rfqstock_.getText().toString();
                rfqItemModel.getprice=holder.tagprice_.getText().toString();
                sendData(rfqItemModel,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return rfqItemModels.size();
    }

    public void removeItem(int pos)
    {
        rfqItemModels.remove(pos);
        notifyDataSetChanged();
    }

    public class RFQItemViewHolder extends RecyclerView.ViewHolder{


        ConstraintLayout confirm;
        ImageView imageView;

        EditText rfqstock_,tagprice_;
        TextView rfqitem;
        public RFQItemViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rfq_img);
            rfqstock_=itemView.findViewById(R.id.rfqstock_);
            tagprice_=itemView.findViewById(R.id.tagprice_);
            rfqitem=itemView.findViewById(R.id.rfqitem);
            confirm=itemView.findViewById(R.id.con_v);


        }
    }



}
