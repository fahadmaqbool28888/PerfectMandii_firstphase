package com.vendor.perfectmandii.Adapter.Vendor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Activity.vendor.dispatchActivity;
import com.vendor.perfectmandii.Model.ModelPaid.OrderModel;
import com.vendor.perfectmandii.Model.vendor.VendorConfirmoRDER;
import com.vendor.perfectmandii.Model.vendor.neworderinfo;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class paidorder extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<OrderModel> data= Collections.emptyList();


    String category;


    String[] av;
    String setca;
    String session;
    // create constructor to innitilize context and data sent from MainActivity
    public paidorder(Context context, List<OrderModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.paid_ordertray, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
       MyHolder myHolder= (MyHolder) holder;
        OrderModel current=data.get(position);

        myHolder.invoiceid_.setText("Order #"+current.customer_order);
        myHolder.subtotalbill_.setText("Subtotal Rs : "+current.subtotal);
        myHolder.fba_.setText(current.Destination);
        myHolder.shippingdate_.setText("Shipping Date: "+current.date);  myHolder.invoiceid_.setText("Order #"+current.customer_order);
        myHolder.subtotalbill_.setText("Subtotal Rs : "+current.subtotal);
        myHolder.fba_.setText(current.Destination);
        myHolder.shippingdate_.setText("Shipping Date: "+current.date);


        myHolder.ordertry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, dispatchActivity.class);


                intent.putExtra("packaging",current.packaging);
                intent.putExtra("subtotal",current.subtotal);
                intent.putExtra("grandtotal",current.grandtotal);
                intent.putExtra("discount",current.discount);
                intent.putExtra("userid",current.userid);
                intent.putExtra("orderid",current.orderid);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);


            }
        });


      /*  category=current.servicename;
        //session=current.session;
        Picasso.get().load(current.serviceicon).into(myHolder.ivFish);
        ((MyHolder) holder).ivFish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(current.servicestatus.equalsIgnoreCase("active"))
                {
                    Intent intent=new Intent(context, DashboardActivity.class);
                    context.startActivity(intent);
                 *//*   if (current.session==null)
                    {
                        Intent intent=new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(context,DashboardActivity.class);
                        intent.putExtra("name",current.fishName);
                        intent.putExtra("session",current.session);
                        intent.putExtra("id",current.usid);
                        intent.putExtra("userid",current.userid);
                        intent.putExtra("orderid",current.orderid);
                        context.startActivity(intent);
                        Toast.makeText(context,current.fishName+session,Toast.LENGTH_LONG).show();
                    }*//*
                    Toast.makeText(context,"Active",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context,"Not Active",Toast.LENGTH_LONG).show();
                }
*/

       /*     }
        });*/
    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {


        CardView ordertry;
        TextView invoiceid_,shippingdate_,orderdate_,fba_,subtotalbill_;
        public MyHolder(View itemView)
        {
            super(itemView);
            invoiceid_=itemView.findViewById(R.id.invoiceid_);
            shippingdate_=itemView.findViewById(R.id.shippingdate_);
            orderdate_=itemView.findViewById(R.id.orderdate_);
            fba_=itemView.findViewById(R.id.fba_);
            subtotalbill_=itemView.findViewById(R.id.subtotalbill_);
            ordertry=itemView.findViewById(R.id.order_IF);


        }

    }


}
