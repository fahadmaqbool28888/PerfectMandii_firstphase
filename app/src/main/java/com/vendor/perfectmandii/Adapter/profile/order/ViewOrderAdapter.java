package com.vendor.perfectmandii.Adapter.profile.order;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vendor.perfectmandii.Adapter.InvoiceProductDetail;
import com.vendor.perfectmandii.GlideApp;
import com.vendor.perfectmandii.OrderPlacedModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.ViewOrderDetailActivity;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        {
public static final int CONNECTION_TIMEOUT = 10000;
public static final int READ_TIMEOUT = 15000;
private Context context;
private LayoutInflater inflater;
        List<InvoiceProductDetail> data= Collections.emptyList();

        String category;



// create constructor to innitilize context and data sent from MainActivity
public ViewOrderAdapter(Context context, List<InvoiceProductDetail> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.showcase_product, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyHolder myHolder= (MyHolder) holder;
    InvoiceProductDetail current=data.get(position);

        //category=current.name;

        //Picasso.get().load(R.drawable.plastic).into(myHolder.ivFish);
        // myHolder.textFishName.setText(current.name);


    myHolder.NameProduct.setText(current.Product_Name);
    myHolder.subtotaltag.setText("Sub Total : "+current.total_price);
    myHolder.pricetag.setText("Price: "+current.selling_price);
    myHolder.quantityNuma.setText("Quantity: "+current.order_quantity);

    if (current.image_path.contains("https://sellerportal.perfectmandi.com/"))
    {
        Glide.with(context)
                .load(current.image_path)
                .into(myHolder.circleImageView);
    }
    else {

        String urls="https://sellerportal.perfectmandi.com/"+current.image_path;
        Glide.with(context)
                .load(urls)
                .into(myHolder.circleImageView);


    }








        }




// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }


            class MyHolder extends RecyclerView.ViewHolder{

    TextView  subtotaltag,NameProduct,quantityNuma,pricetag;



    ImageView circleImageView;

    CheckBox accept_order;
                // create constructor to get widget reference
                public MyHolder(View itemView) {
                    super(itemView);



                    circleImageView=itemView.findViewById(R.id.ci_Invoice);
                    subtotaltag=itemView.findViewById(R.id.subtotaltag);
                    NameProduct=itemView.findViewById(R.id.NameProduct);
                    quantityNuma=itemView.findViewById(R.id.quantityNuma);
                    pricetag=itemView.findViewById(R.id.pricetag);

                    //   textFishName=itemView.findViewById(R.id.textname);


                }

            }

}