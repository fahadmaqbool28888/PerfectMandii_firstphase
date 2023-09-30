package com.vendor.perfectmandii.Adapter.profile.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Adapter.InvoiceProductDetail;
import com.vendor.perfectmandii.AdapterInterface;
import com.vendor.perfectmandii.InTransit_UPdate;
import com.vendor.perfectmandii.IntansitAdapterInterface;
import com.vendor.perfectmandii.OrderPlacedModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.ViewOrderDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntrasnitOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
            List<InvoiceProductDetail> datas;
            IntansitAdapterInterface listener;
            ViewOrderAdapter mAdapter;
            InTransit_UPdate inTransit_uPdate;
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
public IntrasnitOrderAdapter(Context context, List<OrderPlacedModel> data, IntansitAdapterInterface listener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.listener=listener;
        this.inTransit_uPdate=inTransit_uPdate;
        }



// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_transitorder, parent,false);
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

    myHolder.pp_value.setText(current.total);

    myHolder.view_order_id.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if (((MyHolder) holder).action_text121.getText().equals("Close"))
            {
                ((MyHolder) holder).naya.setVisibility(View.GONE);
                ((MyHolder) holder).action_text121.setText("View");








                }
            else {
                ((MyHolder) holder).naya.setVisibility(View.VISIBLE);

                ((MyHolder) holder).action_text121.setText("Close");

/*                try {
                    //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();

                    data=new ArrayList<>();

                    JSONArray jArray = current.jsonArray;

                    // Extract data from json and store into ArrayList as class objects
                    for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        InvoiceProductDetail invoiceProductDetail=new InvoiceProductDetail();
                    *//*        "id": "646",
        "Product_Name": "Water Set Magnate 800 gms",
        "order_quantity": "1",
        "selling_price": "600",
        "total_price": "600",
        "image_path": "https://sellerportal.perfectmandi.com/uploads/9/Product/Water Set Mt 800 gms.jpg"
   *//*
                        invoiceProductDetail.id=json_data.getString("id");
                        invoiceProductDetail.Product_Name=json_data.getString("Product_Name");
                        invoiceProductDetail.order_quantity=json_data.getString("order_quantity");
                        invoiceProductDetail.selling_price=json_data.getString("selling_price");
                        invoiceProductDetail.total_price=json_data.getString("total_price");
                        String str=json_data.getString("total_price");

                        invoiceProductDetail.image_path=json_data.getString("image_path");
              *//*      invoiceProductDetail.product_measure_in=json_data.getString("product_measure_in");
                    invoiceProductDetail.measure_category=json_data.getString("measure_category");
*//*
                        datas.add(invoiceProductDetail);
                    }

                    Toast.makeText(context,String.valueOf(datas.size()),Toast.LENGTH_LONG).show();
                    // Setup and Handover data to recyclerview

                    mAdapter = new ViewOrderAdapter(context, datas);
                    ((MyHolder) holder).order_subtle.setAdapter(mAdapter);

                    ((MyHolder) holder).order_subtle.setLayoutManager( new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
                } catch (Exception e) {
                    //  Toast.makeText(Function.this, e.toString(), Toast.LENGTH_LONG).show();
                }*/
            }


       /*     Intent intent=new Intent( context , ViewOrderDetailActivity.class);
            intent.putExtra("pi",current.purchaseInvoice);
            intent.putExtra("ui",current.userid);
            context.startActivity(intent);*/
        }
    });

    myHolder.confirm_click.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            removeAt(position);
            if(listener != null)


                listener.onClick(current.purchaseInvoice);


        }



    });


        }


            public void removeAt(int position) {
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }

// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }


            class MyHolder extends RecyclerView.ViewHolder{

    TextView pp_id,action_text121,pp_value;
    CardView view_order_id,confirm_click;


    RecyclerView order_subtle;
    LinearLayout naya;

                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);

                    pp_id=itemView.findViewById(R.id.pp_id);
                    view_order_id=itemView.findViewById(R.id.view_order_id);
                    confirm_click=itemView.findViewById(R.id.confirm_click);
                    action_text121=itemView.findViewById(R.id.action_text121);
                    naya=itemView.findViewById(R.id.naya);
                    pp_value=itemView.findViewById(R.id.pp_value);
                    order_subtle=itemView.findViewById(R.id.order_subtle);
                    //   textFishName=itemView.findViewById(R.id.textname);


                }

            }

}