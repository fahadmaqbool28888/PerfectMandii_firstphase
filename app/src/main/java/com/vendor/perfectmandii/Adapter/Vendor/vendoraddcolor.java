package com.vendor.perfectmandii.Adapter.Vendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.vendor.vendorcoloradd;
import com.vendor.perfectmandii.Model.vendor.vendorproadd;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class vendoraddcolor extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    private LayoutInflater inflater;
    List<String> data= Collections.emptyList();


    String category;


    // create constructor to innitilize context and data sent from MainActivity
    public vendoraddcolor(Context context, List<String> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.colortag, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        //=data.get(position);

      //  Toast.makeText(context,current.name,Toast.LENGTH_SHORT).show();
        myHolder.selectcolor.setText(data.get(position));

     //   category=current.id;
        //session=current.session;





    }
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {


        TextView selectcolor;

        public MyHolder(View itemView)
        {
            super(itemView);
       //     ivFish= itemView.findViewById(R.id.addpic_);
            selectcolor=itemView.findViewById(R.id.selectedcolor_1234);

        }

    }


}