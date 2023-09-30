package com.vendor.perfectmandii.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.ProductModel.ProductModel;
import com.vendor.perfectmandii.ProductProfileActivity;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ashu on 6/2/17.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<ProductModel> chapters;
    private LayoutInflater inflater;

    public ChapterAdapter(Context context, ArrayList<ProductModel> chapters) {
        this.context = context;
        this.chapters = chapters;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.single_chapter, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductModel chapter = chapters.get(position);
        holder.tvChapterName.setText(chapter.productName);
        Picasso.get().load(chapter.imageUrl).into(holder.ivChapter);

        holder.ivChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, ProductProfileActivity.class);
                context.startActivity(intent);

            }
        });

        holder.cartclick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, ProductProfileActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivChapter;
        public TextView tvChapterName;
        LinearLayout cartclick;

        public CustomViewHolder(View itemView) {
            super(itemView);

            tvChapterName = (TextView) itemView.findViewById(R.id.tvChapterName);
            ivChapter = (ImageView) itemView.findViewById(R.id.ivChapter);
            cartclick=itemView.findViewById(R.id.click_item);
        }
    }
}
