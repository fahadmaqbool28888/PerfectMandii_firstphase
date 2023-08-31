package com.customer.perfectcustomer.Adapter.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Model.getorder;
import com.customer.perfectcustomer.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptergetdetailPlacedOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    List<getorder> data= Collections.emptyList();


    public AdaptergetdetailPlacedOrder(Context context, List<getorder> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.itemdetail, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        getorder current=data.get(position);
        myHolder.productname.setText(current.Product_Name);
        myHolder.sellingprice.setText(current.selling_price);
        myHolder.productquantity.setText(current.order_quantity);
        myHolder.productsubtotal.setText(current.total_price);
        Picasso.get().load(current.image_path).into(myHolder.productimage);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {

        TextView productname,sellingprice,productquantity,productsubtotal;
        CircleImageView productimage;
        public MyHolder(View itemView) {
            super(itemView);

            productname=itemView.findViewById(R.id.getproductname);
            sellingprice=itemView.findViewById(R.id.getsellingprice);
            productquantity=itemView.findViewById(R.id.getquantity);
            productsubtotal=itemView.findViewById(R.id.getsubtotal);
            productimage=itemView.findViewById(R.id.ivFish_getproductimage);



        }

    }

}