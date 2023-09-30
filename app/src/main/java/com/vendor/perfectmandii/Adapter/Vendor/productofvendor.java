package com.vendor.perfectmandii.Adapter.Vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Model.ProductModel.ProductModel;
import com.vendor.perfectmandii.R;

import java.util.List;

public class productofvendor extends RecyclerView.Adapter<productofvendor.MyHolder>
{
    Context context;List<ProductModel> data;
    LayoutInflater inflater;

    public productofvendor(Context context, List<ProductModel> data)
    {
    this.context=context;
    inflater=LayoutInflater.from(context);
    this.data=data;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.productofvendor,parent,false);
       MyHolder vHolder=new MyHolder(view);
        return vHolder;
    }

    @Override
    public void onBindViewHolder( MyHolder holder, int position) {

        MyHolder myHolder=(MyHolder)holder;
        ProductModel productModel=data.get(position);
        holder.vp_nam.setText(productModel.productName);
        Glide.with(context).load(productModel.imageUrl).into(holder.vp_img);

       /* Intent intent = new Intent("custom-message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("nam",productModel.productName);
        intent.putExtra("img",productModel.imageUrl);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        TextView vp_nam;
        ImageView vp_img;

        MyHolder(View itemView)
        {
            super(itemView);
            vp_nam=itemView.findViewById(R.id.vp_nam);
            vp_img=itemView.findViewById(R.id.vp_img);

        }


    }
}
