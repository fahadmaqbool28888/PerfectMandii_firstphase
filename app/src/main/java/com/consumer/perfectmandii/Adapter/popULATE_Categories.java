package com.consumer.perfectmandii.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Activity.VendorStore.StoreVendorActivity;
import com.consumer.perfectmandii.Adapter.SubCatogories.subItemAdapter;
import com.consumer.perfectmandii.Adapter.SubCatogories.subcategoriesAdapter;
import com.consumer.perfectmandii.Model.Category_INMENU;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategories;
import com.consumer.perfectmandii.ProductProfileActivity;
import com.consumer.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class popULATE_Categories extends RecyclerView.Adapter<popULATE_Categories.MyHolder>
{
    List<Category_INMENU> category_inmenus,category_inmenus_1;
    LayoutInflater layoutInflater;

    Context context;
    subItemAdapter subitem;
    JSONArray data;

    public popULATE_Categories(Context context, JSONArray data)
    {
        layoutInflater=LayoutInflater.from(context);
        this.context=context;
        this.data=data;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.category_dp,parent,false);
        MyHolder myHolder= new MyHolder(view);

        return myHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        try {
            category_inmenus_1=fetechjsonArray(data);
            Category_INMENU productSub=category_inmenus_1.get(position);
          String asr=  productSub.category_name;
         String   string = asr.replace("_", " ");
            holder.categoryName_panel.setText(string);
            if (productSub.category_active.equalsIgnoreCase("1"))
            {
                holder.categoryName_panel.setText(productSub.category_name);

               // holder.categoryName_panel.setBackgroundColor(R.color.opv1);
                Intent intent = new Intent("custom-message");
                intent.putExtra("flag",productSub.category_name);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
            else
            {

            }


            holder.categoryName_panel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                  //  holder.categoryName_panel.setBackgroundColor(R.color.opv1);
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("flag",productSub.category_name);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            // Glide.with(context).load(productSub.product_image_path).into(holder.ivFish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return data.length();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView categoryName_panel;
        RecyclerView recyclerView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            categoryName_panel=itemView.findViewById(R.id.text_link);

        }
    }



    List<Category_INMENU> fetechjsonArray(JSONArray jsonArray) throws JSONException
    {
        category_inmenus=new ArrayList<>();
        for(int k=0;k<jsonArray.length();k++)
        {
            JSONObject pr_data = jsonArray.getJSONObject(k);
            Category_INMENU category_inmenu=new Category_INMENU();
            category_inmenu.category_name=pr_data.getString("Prodcut_Sub_Category");
            category_inmenu.category_active=pr_data.getString("Open");
            category_inmenus.add(category_inmenu);
        }
        return category_inmenus;

    }
}
