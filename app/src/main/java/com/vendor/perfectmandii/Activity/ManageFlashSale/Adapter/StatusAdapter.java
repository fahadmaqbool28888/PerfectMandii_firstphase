package com.vendor.perfectmandii.Activity.ManageFlashSale.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.ManageFlashSale.model.FlashSaleModel;
import com.vendor.perfectmandii.R;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyHolder>
{
Context context;
LayoutInflater layoutInflater;
List<FlashSaleModel> flashSaleModels;
    public StatusAdapter(Context context,List<FlashSaleModel> flashSaleModels)
    {
        this.context=context;
        this.flashSaleModels=flashSaleModels;
        layoutInflater=LayoutInflater.from(context);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=layoutInflater.inflate(R.layout.list_active_flashsale,parent,false);

        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") int position)
    {
        FlashSaleModel flashSaleModel=flashSaleModels.get(position);
        holder.nameofitem.setText(flashSaleModel.product_name);
        holder.old_price_.setText(flashSaleModel.saleprice);

        Picasso.get().load(flashSaleModel.image_path).into(holder.ivFish_1);

        if (flashSaleModel.status.equalsIgnoreCase("yes"))
        {
            holder.btn_price_.setText("Pause");
        }

        holder.btn_price_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("custom-message");
                action_perform(position,flashSaleModel.id);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });



        holder.ckick_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent("custom-message");
                action_perform(position,flashSaleModel.id);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });


        holder.ckick_button_u.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent = new Intent("custom-message");
                action_perform(position,flashSaleModel.id);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });




    }

    void action_perform(int position,String id)
    {
        Intent intent = new Intent("custom-message");

        intent.putExtra("id",id);

        intent.putExtra("pos",position);
        intent.putExtra("flag","include");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    @Override
    public int getItemCount() {
        return flashSaleModels.size();
    }

    public class MyHolder extends  RecyclerView.ViewHolder
    {
        EditText old_price_;
        ImageView ivFish_1;
        TextView nameofitem;
        TextView btn_price_;
        CardView ckick_button;
        ConstraintLayout ckick_button_u;
        public MyHolder(View itemView) {
            super(itemView);
            nameofitem=itemView.findViewById(R.id.nameofitem);
            old_price_=itemView.findViewById(R.id.old_price_);
            ivFish_1=itemView.findViewById(R.id.ivFish_1);
            //
            ckick_button=itemView.findViewById(R.id.ckick_button);
            ckick_button_u=itemView.findViewById(R.id.inside_v);
            btn_price_=itemView.findViewById(R.id.btn_price_);
        }
    }
}
