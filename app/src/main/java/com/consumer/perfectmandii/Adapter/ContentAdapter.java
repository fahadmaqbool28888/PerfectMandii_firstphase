package com.consumer.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.DataContentAdapter_Cat;
import com.consumer.perfectmandii.FlashSaleActivity;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.ModelDas;
import com.consumer.perfectmandii.R;
import com.consumer.perfectmandii.RecentlyAddedActivity;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyHolder>
{
    Context context;
    List<ModelDas> data;
    LayoutInflater layoutInflater;
    DataContentAdapter dataContentAdapter;
    DataContentAdapter_Cat dataContentAdapter_cat;
    DataContentAdapter_FlashSale dataContentAdapter_flashSale;

    UserModel userModel;
    SQLiteHelper sqLiteHelper;
    List<UserModel> userModelList;

    public ContentAdapter(Context context, List<ModelDas> data)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data=data;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.layout_containerproduct,parent,false);
        MyHolder viewholder=new MyHolder(view);

        //get_Data();
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {



      //  System.out.println(position);
     //   ContentModel contentModel=data.get(position);

        ModelDas contentModel=data.get(position);

        holder.heading_co.setText(contentModel.di_hname);
        Glide.with(context).load(contentModel.banner).into(holder.pic_banner);


        holder.shop_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (contentModel.di_name.equalsIgnoreCase("flashsale"))
                {
                    Intent intent=new Intent(context, FlashSaleActivity.class);
                    context.startActivity(intent);
                }
                else if (contentModel.di_name.equalsIgnoreCase("newadd"))
                {
                    Intent intent=new Intent(context, RecentlyAddedActivity.class);
                    context.startActivity(intent);
                }


            }
        });


        if (contentModel.orien.equalsIgnoreCase("grid"))
        {
            //dataContentAdapter=new DataContentAdapter(context,contentModel.array);
            dataContentAdapter_cat=new DataContentAdapter_Cat(context,contentModel.array);
            holder.opwq.setAdapter(dataContentAdapter_cat);
            holder.opwq.setLayoutManager(new GridLayoutManager(context, 4));
           // holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

            holder.opwq.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));


           // get_Data();

        }
        else
        {
            if (contentModel.di_name.equalsIgnoreCase("flashsale"))
            {
                dataContentAdapter_flashSale=new DataContentAdapter_FlashSale(context,contentModel.array);
                holder.opwq.setAdapter(dataContentAdapter_flashSale);
                holder.opwq.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

            }
            else
            {

                dataContentAdapter=new DataContentAdapter(context,contentModel.array);
                holder.opwq.setAdapter(dataContentAdapter);
                holder.opwq.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            }

        }



        //holder.heading_co.setText(contentModel.di_hname);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        RecyclerView opwq;
        TextView heading_co,shop_more;
        CardView cardView;
        ImageView pic_banner;
        MyHolder(View view)
        {
            super(view);
            shop_more=view.findViewById(R.id.shop_more);
            opwq=view.findViewById(R.id.opwq);
            heading_co=view.findViewById(R.id.heading_co);
            cardView=view.findViewById(R.id.main_point);
            pic_banner=view.findViewById(R.id.pic_banner);
        }
    }
}
