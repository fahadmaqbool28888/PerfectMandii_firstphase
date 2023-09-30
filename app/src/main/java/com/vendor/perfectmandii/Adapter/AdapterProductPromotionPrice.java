package com.vendor.perfectmandii.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Adapter.DashboardAdapter.ProductShelfAdapter;
import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public abstract class AdapterProductPromotionPrice extends RecyclerView.Adapter<AdapterProductPromotionPrice.MyHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<StockProductModel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    int count=0;
    private final boolean[] mCheckedStateA;

    SparseBooleanArray[] checkstatearray;

    int total,total1,value1;
    // create constructor to innitilize context and data sent from MainActivity
    public abstract void toggle(String id,String price,String status);

    public AdapterProductPromotionPrice(Context context, List<StockProductModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        mCheckedStateA = new boolean[data.size()];
        checkstatearray=new SparseBooleanArray[data.size()];
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.list_rowvpr, parent,false);
        MyHolder holder=new MyHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        StockProductModel current=data.get(position);

        if (current.path.contains("https://sellerportal.perfectmandi.com/"))
        {
            Picasso.get().load(current.path).into(myHolder.produ_image);
        }
        else
        {
            String url="https://sellerportal.perfectmandi.com/"+current.path;
            Picasso.get().load(url).into(myHolder.produ_image);
        }


        myHolder.nameofitem.setText(current.name);
        myHolder.old_price_.setText(current.price);




        myHolder.switch1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String newprice=myHolder.new_price_.getText().toString();

                if (newprice.equalsIgnoreCase(""))
                {
                    boolean state=data.get(position).isChecked();

                    if (!state)
                    {
                        myHolder.switch1_3.setChecked(false);
                        Toast.makeText(context,"Please enter sale price before proceeding",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        myHolder.new_price_.setText("");
                        toggle(current.id,null,null);

                    }

                }
                else {
                    boolean state=data.get(position).isChecked();

                    data.get(position).setChecked(!state);
                    if (!state)
                    {
                        String price=myHolder.new_price_.getText().toString();
                        toggle(current.id,price,"yes");
                    }
                    else
                    {
                        myHolder.new_price_.setText("");
                        toggle(current.id,null,null);

                    }
                }





            }
        });

        myHolder.switch1_3.setChecked(data.get(position).isChecked());



    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {
        EditText  old_price_,new_price_;
        TextView stock_a,stock_e,miq_moq,nameofitem;
        ImageView produ_image;
        CheckBox checkBox;

        TextView btn_price_;
        CardView ckick_button;
        ConstraintLayout ckick_button_u;

        Switch switch1_3;

        public MyHolder(View itemView) {
            super(itemView);
            stock_a=itemView.findViewById(R.id.stock_a);


            switch1_3=itemView.findViewById(R.id.switch1_3);
            nameofitem=itemView.findViewById(R.id.nameofitem);
            produ_image=itemView.findViewById(R.id.ivFish_1);

            old_price_=itemView.findViewById(R.id.old_price_);
            new_price_=itemView.findViewById(R.id.new_price_);

        }

    }

}