package com.vendor.perfectmandii.Adapter.Vendor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.vendor.AddProductActivity;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.Model.vendor.vendorproadd;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

import static com.vendor.perfectmandii.R.drawable.optimizedlogo;

public class vendoraddproduct extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    private LayoutInflater inflater;
    List<Bitmap> litbitmap= Collections.emptyList();

    Bitmap bitmap;


    String category;


    // create constructor to innitilize context and data sent from MainActivity
    public vendoraddproduct(Context context, List<Bitmap> litbitmap){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.litbitmap=litbitmap;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.addpicont, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;


/*        vendorproadd current=data.get(position);
        category=current.id;*/
        //session=current.session;
        bitmap=litbitmap.get(position);
        if (bitmap==null)
        {
            myHolder.ivFish.setImageResource(R.drawable.addplus);
        }
        else
        {
            myHolder.ivFish.setImageBitmap(litbitmap.get(position));
        }
       // myHolder.ivFish.setImageBitmap(litbitmap.get(position));

       /* myHolder.addproductbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context,"Click me",Toast.LENGTH_LONG).show();
            }
        });*/


        myHolder.ivFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("flag","one");

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);



            }
        });





    }
    @Override
    public int getItemCount()
    {
        return litbitmap.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {


        //CardView addproductbutton;
       ImageView ivFish;
        public MyHolder(View itemView)
        {
            super(itemView);
            ivFish= itemView.findViewById(R.id.addpic_);
           // addproductbutton=itemView.findViewById(R.id.addproductbtn_);

        }

    }




}