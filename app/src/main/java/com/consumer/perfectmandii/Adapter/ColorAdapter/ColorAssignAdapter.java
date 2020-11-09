package com.consumer.perfectmandii.Adapter.ColorAdapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.consumer.perfectmandii.R;

import java.util.List;

public class ColorAssignAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    int count=0;
    Context context;
    List<String> data;
    LayoutInflater layoutInflater;
    public ColorAssignAdapter(Context context, List<String> data)
    {
        this.context=context;
        this.data=data;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        layoutInflater=LayoutInflater.from(context);
        View rootview=layoutInflater.inflate(R.layout.colorpallette_la,parent,false);
        MyHolder myHolder=new MyHolder(rootview);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        MyHolder myHolder= (MyHolder) holder;
        myHolder.linearLayout.setBackgroundColor(Color.YELLOW);
        myHolder.linearLayout.setOnClickListener(new View.OnClickListener()
        {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (count==0)
                {
                    myHolder.linearLayout.setForeground(context.getDrawable(R.drawable.card_view_border));
                    count=count+1;
                }
                else
                {
                    myHolder.linearLayout.setForeground(context.getDrawable(R.drawable.card_view_border_1));
                    myHolder.linearLayout.setBackgroundColor(Color.YELLOW);
                    Toast.makeText(context,"Single color select againt this product",Toast.LENGTH_LONG).show();
                    count=count-1;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {

        CardView linearLayout;

        public MyHolder(View itemView)
        {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.colorpallete_block);
        }
    }
}
