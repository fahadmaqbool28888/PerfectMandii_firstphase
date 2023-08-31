package com.customer.perfectcustomer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Adapter.SubCatogories.subItemAdapter;
import com.customer.perfectcustomer.Model.Category_INMENU;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract  class popULATE_Categories extends RecyclerView.Adapter<popULATE_Categories.MyHolder>
{
    List<Category_INMENU> category_inmenus,category_inmenus_1;
    LayoutInflater layoutInflater;

    Context context;
    subItemAdapter subitem;
    JSONArray data;

    String flag;
    public popULATE_Categories(Context context, JSONArray data,String flag)
    {
        layoutInflater=LayoutInflater.from(context);
        this.context=context;
        this.data=data;
        this.flag=flag;
    }


   public abstract void method(String item);

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

            holder.mytab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    method(string);
                }
            });
            if (position==0)
            {
                method(string);
            }




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
        CardView mytab;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            categoryName_panel=itemView.findViewById(R.id.text_link);

            mytab=itemView.findViewById(R.id.mytab);
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
