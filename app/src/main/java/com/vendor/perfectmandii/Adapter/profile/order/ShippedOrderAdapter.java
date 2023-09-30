package com.vendor.perfectmandii.Adapter.profile.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.IntansitAdapterInterface;
import com.vendor.perfectmandii.OrderPlacedModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.ViewOrderDetailActivity;

import java.util.Collections;
import java.util.List;

public class ShippedOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
            IntansitAdapterInterface listener;
            public static final int CONNECTION_TIMEOUT = 10000;
            public static final int READ_TIMEOUT = 15000;
            private Context context;
            private LayoutInflater inflater;
        List<OrderPlacedModel> data= Collections.emptyList();

        String category;


            onItemClickListner onItemClickListner;


            public interface onItemClickListner{
                void onClick(String str);//pass your object types.
            }


// create constructor to innitilize context and data sent from MainActivity
public ShippedOrderAdapter(Context context, List<OrderPlacedModel> data, IntansitAdapterInterface listener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.listener=listener;
        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_shippedorder, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
{
        MyHolder myHolder= (MyHolder) holder;
        OrderPlacedModel current=data.get(position);
        myHolder.pp_id.setText(current.purchaseInvoice);
}




// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }


            class MyHolder extends RecyclerView.ViewHolder{

    TextView pp_id;




                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);

                    pp_id=itemView.findViewById(R.id.pp_id);


                    //   textFishName=itemView.findViewById(R.id.textname);


                }

            }

}