package com.customer.perfectcustomer.Adapter.Order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Activity.Home.Order.InvoiceView;
import com.customer.perfectcustomer.Model.DataFish;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.Activity.CustomerOrder.OrderDetailActivity;
import com.customer.perfectcustomer.R;

import java.util.Collections;
import java.util.List;

public class AdapterPlacedOrder extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    List<OrderCartModel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    CardView order_IF;


    int total,value,total1,value1;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterPlacedOrder(Context context, List<OrderCartModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.container_ordertray, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {


        MyHolder myHolder= (MyHolder) holder;
        OrderCartModel current=data.get(position);
        category=current.name;
        myHolder.getOrderno.setText(current.customer_order);
        myHolder.getinvoiceid.setText(current.invoice_order);
        myHolder.getplacedate.setText(current.order_place);
        myHolder.getShippingdate.setText("");
        myHolder.paymentstatus.setText(current.paid_status);
        myHolder.gettotalbill.setText(current.grandtotal);


        order_IF.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (current.paid_status.equalsIgnoreCase("processed"))
                {
                   Intent intent=new Intent(context, OrderDetailActivity.class);
                   intent.putExtra("customer_order",current.customer_order);
                    intent.putExtra("invoice_order",current.invoice_order);
                    intent.putExtra("order_place",current.order_place);
                    intent.putExtra("paid_status",current.paid_status);
                    intent.putExtra("grandtotal",current.grandtotal);
                   context.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(context, InvoiceView.class);
                    intent.putExtra("id",current.id);
                    intent.putExtra("flag","flag");
                    intent.putExtra("userid",current.userid);
                    intent.putExtra("orderid",current.order_id);
                    intent.putExtra("packaging",current.packaging);
                    intent.putExtra("stotal",current.subtotal);
                    intent.putExtra("gtotal",current.grandtotal);
                    intent.putExtra("discount",current.discount);
                    intent.putExtra("sorder",current.customer_order);
                    intent.putExtra("invoice",current.invoice_order);
                    intent.putExtra("session",current.session);
                    intent.putExtra("status",current.paid_status);

                    context.startActivity(intent);
                }

            }
        });
        //Picasso.get().load(R.drawable.optimizedlogo).into(myHolder.ivFish);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView orderno,getOrderno,shippingdate,getShippingdate,totalamount,getTotalamount,orderplace,getOrderplace,status,getStatus,getinvoiceid;



        TextView productQuantity,getplacedate,getshippingdate,paymentstatus,gettotalbill;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            orderno=itemView.findViewById(R.id.orderid);
            getOrderno=itemView.findViewById(R.id.getorderid);
            getinvoiceid=itemView.findViewById(R.id.getinvoiceid);
            getplacedate=itemView.findViewById(R.id.getplacedate);
            getShippingdate=itemView.findViewById(R.id.shippingdate);
            paymentstatus=itemView.findViewById(R.id.paymentstatus);
            gettotalbill=itemView.findViewById(R.id.totalbill_);
            order_IF=itemView.findViewById(R.id.order_IF);



        }

    }

}