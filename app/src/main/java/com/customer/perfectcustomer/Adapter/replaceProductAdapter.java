package com.customer.perfectcustomer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class replaceProductAdapter  extends RecyclerView.Adapter<replaceProductAdapter.ReplaceViewHolder>
{
    Context context;
    LayoutInflater layoutInflater;
    JSONArray data;

    List<OrderCartModel> product;
    List<OrderCartModel> productSubs;
    int pos;
    replaceProductAdapter(Context context, JSONArray data,int pos)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data=data;
        this.pos=pos;
    }



    public abstract  void select_val(int postion,boolean val,int pos,OrderCartModel productSub);


    @Override
    public ReplaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.replace_product,parent,false);

        return new ReplaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReplaceViewHolder holder,@SuppressLint("RecyclerView")  int position)
    {
        try {
            product=fetechjsonArray(data);
            OrderCartModel productSub=product.get(position);

            holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-");
            if (productSub.measure_category.equalsIgnoreCase("Kilogram"))
            {
                holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-" +productSub.measure_category);


                Double total=Double.parseDouble(productSub.product_measure_in)*Double.parseDouble(productSub.order_quantity)*Double.parseDouble(productSub.selling_price);

                productSub.val=total;
                int val=total.intValue();
                holder.rp_total.setText("Total: PKR "+ val +" /-");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Set"))
            {
                holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-" +productSub.measure_category);

                productSub.val=Double.parseDouble(productSub.order_quantity)*Double.parseDouble(productSub.selling_price);
                int val=Integer.parseInt(productSub.order_quantity)*Integer.parseInt(productSub.selling_price);
                holder.rp_total.setText("Total: PKR "+ val +" /-");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Piece"))
            {
                holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-" +productSub.measure_category);
                int val=Integer.parseInt(productSub.order_quantity)*Integer.parseInt(productSub.selling_price);
                productSub.val=Double.parseDouble(productSub.order_quantity)*Double.parseDouble(productSub.selling_price);

                holder.rp_total.setText("Total: PKR "+ val +" /-");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Dozen"))
            {
                holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-" +productSub.measure_category);
                productSub.val=Double.parseDouble(productSub.order_quantity)*Double.parseDouble(productSub.selling_price);

                int val=Integer.parseInt(productSub.order_quantity)*Integer.parseInt(productSub.selling_price);
                holder.rp_total.setText("Total: PKR "+ val +" /-");
            }
            else
            {
                productSub.val=Double.parseDouble(productSub.order_quantity)*Double.parseDouble(productSub.selling_price);
                holder.rp_pkr.setText("PKR "+productSub.selling_price+"/-" +productSub.measure_category);

            }


            holder.rp_q.setText(productSub.order_quantity +" "+productSub.measure_category);
            holder.rp_name.setText(productSub.name);
            if (productSub.image_urlname.contains("https://sellerportal.perfectmandi.com/"))
            {
                Glide.with(context).load(productSub.image_urlname).into(holder.imgpth);
            }
            else
            {
                String urls="https://sellerportal.perfectmandi.com/"+productSub.image_urlname;
                Picasso.get().load(urls).into(holder.imgpth);

            }


            holder.replace_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    System.out.println("this is "+" "+product.get(position).toString());


                    select_val(position,true,pos,productSub);
                }
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public class ReplaceViewHolder extends  RecyclerView.ViewHolder {

        ImageView  imgpth;
        TextView rp_name,rp_pkr,rp_q,rp_total;

        Button replace_;
        public ReplaceViewHolder(View itemView)
        {
            super(itemView);
            rp_total=itemView.findViewById(R.id.rp_total);
            imgpth=itemView.findViewById(R.id.imgpth);
            rp_name=itemView.findViewById(R.id.rp_name);
            rp_pkr=itemView.findViewById(R.id.rp_pkr);
            rp_q=itemView.findViewById(R.id.rp_q);
            replace_=itemView.findViewById(R.id.replace_);
        }
    }


    List<OrderCartModel> fetechjsonArray(JSONArray jsonArray) throws JSONException
    {
        productSubs=new ArrayList<>();
        for(int k=0;k<jsonArray.length();k++)
        {
            /*            fishData.id=json_data.getString("id");
                    fishData.image_urlname= json_data.getString("image_url");
                    fishData.name= json_data.getString("name");

                    System.out.println("From shopping "+json_data.getString("name"));
                    fishData.Accountid= json_data.getString("Accountid");
                    fishData.l2_product_name= json_data.getString("l2_product_name");
                    fishData.category_provider= json_data.getString("category_provider");
                    fishData.status= json_data.getString("status");
                    fishData.price=json_data.getString("price");
                    fishData.order_quantity=json_data.getString("cq");
                    fishData.selling_price=json_data.getString("cp");
                    fishData.st_price=json_data.getString("total_price");

                    totalvalue=Integer.parseInt(fishData.st_price);
                    total_value.add(Integer.parseInt(fishData.st_price));
                    fishData.productDescription=json_data.getString("Product_Description");
                    fishData.ocid=json_data.getString("ocid");
                    fishData.stock=json_data.getString("stock");
                    fishData.MOQ=json_data.getString("MOQ");
                    fishData.replaceData=json_data.getJSONArray("replace");
                    fishData.measure_category=json_data.getString("measure_category");
                    fishData.product_measure_in=json_data.getString("product_measure_in");*/

            JSONObject pr_data = jsonArray.getJSONObject(k);
            OrderCartModel productSub=new OrderCartModel();
            productSub.id=pr_data.getString("id");

            productSub.selling_price=pr_data.getString("price");

            String quantity=pr_data.getString("qunatity");
            String price=pr_data.getString("price");
            productSub.order_quantity=pr_data.getString("qunatity");
            productSub.Sub_Category=pr_data.getString("Prodcut_Sub_Category");
            productSub.Principle_Category=pr_data.getString("Principle_Category");
            productSub.name=pr_data.getString("Product_Name");
            productSub.productDescription=pr_data.getString("Product_Description");
            productSub.category_provider=pr_data.getString("Product_Provider");
            productSub.product_measure_in=pr_data.getString("product_measure_in");
            productSub.measure_category=pr_data.getString("measure_category");


            productSub.image_urlname=pr_data.getString("image_path");
            productSub.ocid=pr_data.getString("refrence");
            productSub.refrence=pr_data.getString("refrence");
            productSub.replaceData=pr_data.getJSONArray("replace");
            productSub.st_price=pr_data.getString("total_price");

            productSub.oos=pr_data.getString("oos");
            productSub.rp=pr_data.getString("rp");




            productSubs.add(productSub);
        }
        return productSubs;

    }
}
