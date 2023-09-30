package com.vendor.perfectmandii.Activity.RFQ.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Activity.RFQ.model.DetailRFQ;
import com.vendor.perfectmandii.R;

import java.util.List;

public abstract class DetailRFQIAdapter extends RecyclerView.Adapter<DetailRFQIAdapter.RFQItemViewHolder>
{

    Context context;
    List<DetailRFQ> rfqItemModels;
    LayoutInflater layoutInflater;


    public DetailRFQIAdapter(Context context, List<DetailRFQ> rfqItemModels)
    {
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        this.rfqItemModels = rfqItemModels;
    }

    @Override
    public RFQItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.activity_detailrfq_1,parent,false);
        return new RFQItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RFQItemViewHolder holder, @SuppressLint("RecyclerView") int position)
    {


        DetailRFQ rfqItemModel=rfqItemModels.get(position);

        Glide.with(context).load(rfqItemModel.img).into(holder.imageView);

        holder.rfqstock_.setText(rfqItemModel.quan);
        holder.tagprice_.setText(rfqItemModel.price);
        holder.rfqitem.setText(rfqItemModel.name);

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String stock=holder.rfqstock_.getText().toString();
              String price=  holder.tagprice_.getText().toString();
              int pric=Integer.parseInt(price);
                rfqItemModel.getstock=stock;
                rfqItemModel.getprice=holder.tagprice_.getText().toString();
                int st=Integer.parseInt(stock);


                if (st<1)
                {
                    operationPerform("oos",rfqItemModel,position);
                }
                else
                {
                    if (pric>0)
                    {
                        operationPerform("confirm",rfqItemModel, position);
                    }
                    else {
                        Toast.makeText(context,"Please submit price",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        holder.replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String stock=holder.rfqstock_.getText().toString();
                rfqItemModel.getstock=stock;

                rfqItemModel.getprice=holder.tagprice_.getText().toString();
                operationPerform("replace",rfqItemModel, position);
            }
        });
        holder.oos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String stock=holder.rfqstock_.getText().toString();
                rfqItemModel.getstock=stock;

                rfqItemModel.getprice=holder.tagprice_.getText().toString();
                operationPerform("oos",rfqItemModel,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rfqItemModels.size();
    }

    public void removeAt(int position) {
        rfqItemModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, rfqItemModels.size());
    }


    public abstract void operationPerform(String flag, DetailRFQ detailRFQ, int position);

    public class RFQItemViewHolder extends RecyclerView.ViewHolder{


        CardView confirm,replace,oos;
        ImageView imageView;

        EditText rfqstock_,tagprice_;
        TextView rfqitem;
        public RFQItemViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.rfq_img);
            rfqstock_=itemView.findViewById(R.id.rfqstock_);
            tagprice_=itemView.findViewById(R.id.tagprice_);
            rfqitem=itemView.findViewById(R.id.rfqitem);

            confirm=itemView.findViewById(R.id.confirm_rfq);
            replace=itemView.findViewById(R.id.replace_rfq);
            oos=itemView.findViewById(R.id.oos_rfq);
        }
    }



}
