package com.vendor.perfectmandii.Adapter.profile.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Activity.OrderStockActivity;
import com.vendor.perfectmandii.AdapterInterface;
import com.vendor.perfectmandii.Interface.FragmentCommunication;
import com.vendor.perfectmandii.Model.modelProduct;
import com.vendor.perfectmandii.OrderPlacedModel;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.ViewOrderDetailActivity;

import java.util.Collections;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
            private FragmentCommunication mCommunicator;
            AdapterInterface listener;
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
public MyOrderAdapter(Context context, List<OrderPlacedModel> data,AdapterInterface listener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.listener=listener;

        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_myorder, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyHolder myHolder= (MyHolder) holder;
        OrderPlacedModel current=data.get(position);
        //category=current.name;

        //Picasso.get().load(R.drawable.plastic).into(myHolder.ivFish);
        // myHolder.textFishName.setText(current.name);


    myHolder.pp_id.setText(current.purchaseInvoice);


    myHolder.view_order_id.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            Intent intent=new Intent( context , ViewOrderDetailActivity.class);
            intent.putExtra("pi",current.purchaseInvoice);
            intent.putExtra("ui",current.userid);
            context.startActivity(intent);
        }
    });

    myHolder.confirm_click.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(listener != null)

                listener.onClick(current.purchaseInvoice);

        }
    });

    myHolder.edit_order_click.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent_m=new Intent( context , OrderStockActivity.class);
            intent_m.putExtra("pi",current.purchaseInvoice);
            intent_m.putExtra("ui",current.userid);
            context.startActivity(intent_m);
        }
    });

        }




// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }


            class MyHolder extends RecyclerView.ViewHolder{

    TextView pp_id;
    ImageView view_order_id,confirm_click,edit_order_click;



                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);

                    pp_id=itemView.findViewById(R.id.pp_id);
                    view_order_id=itemView.findViewById(R.id.view_order_id);
                    confirm_click=itemView.findViewById(R.id.my_order_confirm);
                    edit_order_click=itemView.findViewById(R.id.edit_order_click);

                    //   textFishName=itemView.findViewById(R.id.textname);


                }

            }

}