package com.consumer.perfectmandii.Adapter.SubCatogories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Activity.Product_Profile_extend;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.Model.SubCategories.SubCategoriesRelevant;
import com.consumer.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class subItemAdapter_relevant_1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    LayoutInflater layoutInflater;
    Context context;
    JSONArray data;

    List<SubCategoriesRelevant> datas;
    List<ProductSub> productSubs;
    List<ProductSub> product;
    String userid;
    String session;
    String orientation;
    public subItemAdapter_relevant_1(Context context, List<SubCategoriesRelevant> datas)
    {
        this.context=context;

        layoutInflater=LayoutInflater.from(context);
        this.datas=datas;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=layoutInflater.inflate(R.layout.container_homesi,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;

        SubCategoriesRelevant current = datas.get(position);


        try {
            product=fetechjsonArray(current.jsonArray);
            ProductSub productSub=product.get(position);

            myHolder.ivFish_textName.setText(productSub.product_name);
        //    ProductSub productSub= (ProductSub) data.get(position);
            Glide
                    .with(context)
                    .load(productSub.product_image_path)
                    .centerCrop()
                    .into(myHolder.imageView);


            myHolder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int gp=Integer.parseInt(productSub.product_price);
                    if (gp>0)
                    {
                        Intent intent=new Intent(context, Product_Profile_extend.class);
                        intent.putExtra("value",productSub.product_image_path);
                        intent.putExtra("proid",productSub.product_id);
                        intent.putExtra("price",productSub.product_price);
                        intent.putExtra("userid",userid);
                        intent.putExtra("session",session);
                 /*   intent.putExtra("proid",pro);
                    intent.putExtra("userid",current.usersessionid);
                    intent.putExtra("session",current.usersession);
                    intent.putExtra("price",current.price);
                    intent.putExtra("category",current.parent_Category);*/
                        intent.putExtra("name",productSub.product_name);
                        intent.putExtra("description",productSub.product_description);
                        intent.putExtra("seller",productSub.product_provider);
                        /*  intent.putExtra("username",current.username);*/

                        intent.putExtra("vendor",productSub.product_provider);
                 /*   intent.putExtra("name",current.name);
                    intent.putExtra("path",current.profilepic);*/
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context,"This Product has lisiting issues",Toast.LENGTH_LONG).show();
                    }

                }
            });





        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {

        TextView ivFish_textName;
        ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ivFish);
            ivFish_textName=itemView.findViewById(R.id.ivFish_textName);
        }
    }


    List<ProductSub> fetechjsonArray(JSONArray jsonArray) throws JSONException {


        productSubs=new ArrayList<>();
        for(int k=0;k<jsonArray.length();k++)
        {
            JSONObject pr_data = jsonArray.getJSONObject(k);
            ProductSub productSub=new ProductSub();
            productSub.product_id=pr_data.getString("id");
            productSub.product_name=pr_data.getString("Product_Name");
            productSub.product_description=pr_data.getString("Product_Description");
            productSub.product_price=pr_data.getString("Product_Unit_Price");
            productSub.product_image_path=pr_data.getString("image_url");
            productSub.product_provider=pr_data.getString("Product_Provider");

            productSubs.add(productSub);
        }
        return productSubs;

    }



    }



