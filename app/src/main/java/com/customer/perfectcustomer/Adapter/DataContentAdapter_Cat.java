package com.customer.perfectcustomer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.Home.Fragment.ProductBy.DashboardActivity;
import com.customer.perfectcustomer.Model.ContentModel;
import com.customer.perfectcustomer.Model.SubCategories.ProductSub;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataContentAdapter_Cat extends RecyclerView.Adapter<DataContentAdapter_Cat.MyHolder>
        {
        Context context;
        List<ContentModel> data;
        LayoutInflater layoutInflater;
            JSONArray data_array;

            List<ProductSub> productSubs;
            List<ProductSub> product;
public DataContentAdapter_Cat(Context context, JSONArray data_array)
        {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data_array=data_array;
        }


@Override
public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.category_layout,parent,false);
        MyHolder viewholder=new MyHolder(view);
        return viewholder;
        }

@Override
public void onBindViewHolder(MyHolder holder, int position)
        {
       // ContentModel model=data.get(position);


            System.out.println(data_array);
            try {
                product=fetechjsonArray(data_array);
                ProductSub productSub=product.get(position);


                Glide.with(context).load(productSub.product_image_path).into(holder.ivFish);


                holder.ivFish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, DashboardActivity.class);
                        intent.putExtra("cname",productSub.product_name);
                        intent.putExtra("category",productSub.headingname);
                    /*    intent.putExtra("session",current.session);
                        intent.putExtra("id",current.usid);
                        intent.putExtra("userid",current.userid);
                        intent.putExtra("orderid",current.orderid);
                        intent.putExtra("name",current.username);
                        intent.putExtra("profilepic",current.profilepic);*/
                        context.startActivity(intent);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

@Override
public int getItemCount()
        {
        return data_array.length();
        }

public class MyHolder extends RecyclerView.ViewHolder
{
   ImageView ivFish;
    MyHolder(View view)
    {
        super(view);
        ivFish=view.findViewById(R.id.ivFish_cons);


    }
}

            List<ProductSub> fetechjsonArray(JSONArray jsonArray) throws JSONException {


                productSubs=new ArrayList<>();
                for(int k=0;k<jsonArray.length();k++)
                {
                    JSONObject pr_data = jsonArray.getJSONObject(k);
                    ProductSub productSub=new ProductSub();
                   // productSub.product_id=pr_data.getString("id");
                    productSub.product_name=pr_data.getString("name");
                    productSub.headingname=pr_data.getString("category");
                  //  productSub.product_description=pr_data.getString("Product_Description");
                   // productSub.product_price=pr_data.getString("Product_Unit_Price");
                    productSub.product_image_path=pr_data.getString("image_path");
                    //productSub.product_provider=pr_data.getString("Product_Provider");

                    productSubs.add(productSub);
                }
                return productSubs;

            }
}
