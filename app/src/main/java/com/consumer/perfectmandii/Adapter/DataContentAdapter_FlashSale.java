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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Model.ContentModel;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;
import com.consumer.perfectmandii.ProductProfileActivity;
import com.consumer.perfectmandii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataContentAdapter_FlashSale extends RecyclerView.Adapter<DataContentAdapter_FlashSale.MyHolder>
        {
            Double fr;
        Context context;
        List<ContentModel> data;
        LayoutInflater layoutInflater;
            JSONArray data_array;

            List<ProductSub> productSubs;
            List<ProductSub> product;
public DataContentAdapter_FlashSale(Context context, JSONArray data_array)
        {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data_array=data_array;
        }


@Override
public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.container_flashsale,parent,false);
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

                int proqa=Integer.parseInt(productSub.product_quan);
                int promq=Integer.parseInt(productSub.product_moq);

                if (proqa<=0||proqa<promq)
                {
                    holder.out_of_stock.setVisibility(View.VISIBLE);
                    holder.discount_pan.setVisibility(View.GONE);
                    holder.product_name_.setText(productSub.product_name);
                    Glide.with(context).load(productSub.product_image_path).into(holder.product_img);
                    holder.product_price_.setText("Rs. "+productSub.product_price);
                }
                else
                {
                    Double price=getDiscountedp(productSub.product_promotionPrice,productSub.product_price);


                    int value= (int) (100-price);
                    holder.discountoff.setText(String.valueOf(value)+"% off");
                    holder.product_name_.setText(productSub.product_name);
                    holder.product_price_.setText("Rs. "+productSub.product_promotionPrice);

                    Glide.with(context).load(productSub.product_image_path).into(holder.product_img);


                    holder.product_box_flashsale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int gp=Integer.parseInt(productSub.product_promotionPrice);
                            if (gp>0)
                            {

                                Intent intent=new Intent(context, ProductProfileActivity.class);
                                intent.putExtra("flag","flag");
                                intent.putExtra("name",productSub.product_name);
                                intent.putExtra("path",productSub.product_image_path);
                                intent.putExtra("price",productSub.product_promotionPrice);
                                intent.putExtra("description",productSub.product_description);
                                intent.putExtra("product_id",productSub.product_id);
                                intent.putExtra("moq",productSub.product_moq);
                                intent.putExtra("quan",productSub.product_quan);
                                intent.putExtra("pro",productSub.product_provider);
                                context.startActivity(intent);


                            }
                            else
                            {
                                Toast.makeText(context,"This Product has lisiting issues",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


               // Glide.with(context).load(productSub.product_image_path).into(holder.ivFish);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        Double getDiscountedp(String discountPrice,String OriginalPrice)
        {

            Double dprice=Double.parseDouble(discountPrice);
            Double oprice=Double.parseDouble(OriginalPrice);
            //int difference=oprice-dprice

            System.out.println(dprice+" "+oprice);
            fr= dprice/oprice*100;
            return fr;
        }

@Override
public int getItemCount()
        {
            if (data_array.length()<10)
            {
                return data_array.length();
            }
            else
            {
                return 10;
            }

        }

public class MyHolder extends RecyclerView.ViewHolder
{
    CardView product_box_flashsale;
   ImageView ivFish,product_img;
   TextView discountoff,product_name_,product_price_;
   ConstraintLayout discount_pan,out_of_stock;
    MyHolder(View view)
    {
        super(view);
       // ivFish=view.findViewById(R.id.ivFish_cons);
        out_of_stock=view.findViewById(R.id.out_of_stock);
        discount_pan=view.findViewById(R.id.discount_pan);
        product_price_=view.findViewById(R.id.product_price_);
        product_img=view.findViewById(R.id.product_img);
        discountoff=view.findViewById(R.id.discount_dexs);
        product_name_=view.findViewById(R.id.product_name_);
        product_box_flashsale=view.findViewById(R.id.product_box_flashsale);


    }
}

            List<ProductSub> fetechjsonArray(JSONArray jsonArray) throws JSONException {


                productSubs=new ArrayList<>();
                for(int k=0;k<jsonArray.length();k++)
                {
                    JSONObject pr_data = jsonArray.getJSONObject(k);
                    ProductSub productSub=new ProductSub();
                   // productSub.product_id=pr_data.getString("id");
                    productSub.product_id=pr_data.getString("id");
                    productSub.product_moq=pr_data.getString("moq");
                    productSub.product_name=pr_data.getString("Product_Name");
                    productSub.product_description=pr_data.getString("Product_Description");
                    productSub.product_quan=pr_data.getString("Product_Unit_Quan");
                    productSub.product_price=pr_data.getString("Product_Unit_Price");
                    productSub.product_image_path=pr_data.getString("image_path");
                   // productSub.product_price=pr_data.getString("Product_Unit_Price");
                    productSub.product_promotionPrice=pr_data.getString("promotion_price");
                    productSub.product_provider=pr_data.getString("Product_Provider");

                    productSubs.add(productSub);
                }
                return productSubs;

            }
}
