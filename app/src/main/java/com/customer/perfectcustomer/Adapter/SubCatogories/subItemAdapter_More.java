package com.customer.perfectcustomer.Adapter.SubCatogories;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Model.SubCategories.ProductSub;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.ProductProfileActivity;
import com.customer.perfectcustomer.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class subItemAdapter_More extends RecyclerView.Adapter<subItemAdapter_More.MyHolder>
{
    int quan,gp;
    int mquan;
    Double fr;
    boolean check = true;
    String imagepath,userid,session,ProductPrice,productname,productDescription,proid,id,username,profilepic,newOrder,categoryname,ordernum_dialog;
    String req_prod_id,req_userid,req_id,req_session,req_count,req_productPrice,req_gtotal,req_neworder;
    String ServerUploadPath ="https://staginigserver.perfectmandi.com/addcartitem.php" ;
    ProgressDialog pdLoading;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    CardView atc_dialog;
    AlertDialog alertDialog;
    LayoutInflater layoutInflater;
    Context context;
    JSONArray data;
    List<ProductSub> productSubs;
    List<ProductSub> product;

    private final boolean[] mCheckedStateA;
    public subItemAdapter_More(Context context, JSONArray data, boolean[] mCheckedStateA)
    {
        this.context=context;
        this.data=data;
        layoutInflater=LayoutInflater.from(context);

        this.mCheckedStateA = mCheckedStateA;
    }
   public subItemAdapter_More(Context context, JSONArray data, String userid, String session, String categoryname)
    {
        this.context=context;
        this.data=data;
        layoutInflater=LayoutInflater.from(context);
        this.userid=userid;
        this.session=session;
        this.categoryname=categoryname;
        mCheckedStateA=new boolean[data.length()];
    }

    List<ProductSub> fetechjsonArray(JSONArray jsonArray) throws JSONException
    {
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
            productSub.product_moq=pr_data.getString("moq");
            productSub.product_quan=pr_data.getString("quan");
            productSub.product_provider=pr_data.getString("Product_Provider");
            productSub.product_promotionPrice=pr_data.getString("promotion_price");
            productSub.pstatus=pr_data.getString("promotion_status");
            productSub.measure_category=pr_data.getString("measure_category");
            productSub.product_measure_in=pr_data.getString("product_measure_in");


            if(pr_data.getString("Product_Unit_Price")!=null && pr_data.getString("moq")!=null && pr_data.getString("quan")!=null && pr_data.getString("Product_Provider")!=null && pr_data.getString("promotion_status")!=null)
            {
                productSubs.add(productSub);
            }

        }
        return productSubs;

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

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.container_flashsale_1,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        try {
            product=fetechjsonArray(data);
            ProductSub productSub=product.get(position);


            holder.product_name_.setText(productSub.product_name);
          //  Glide.with(context).load(productSub.product_image_path).into(holder.product_img);

            if (productSub.product_image_path.contains("https://sellerportal.perfectmandi.com/"))
            {
                Picasso.get().load(productSub.product_image_path).into(holder.product_img);
                //  Glide.with(context).load(productSub.product_image_path).into(holder.product_img);
            }
            else {
                String url="https://sellerportal.perfectmandi.com/"+productSub.product_image_path;
                // Glide.with(context).load(url).into(holder.product_img);
                Picasso.get().load(url).into(holder.product_img);

            }
            if (productSub.pstatus.equalsIgnoreCase("yes"))
            {
                holder.product_price_.setText("Rs. "+productSub.product_promotionPrice);
                Double price=getDiscountedp(productSub.product_promotionPrice,productSub.product_price);
                int value= (int) (100-price);
                mCheckedStateA[position] = true;
                holder.discountoff.setText(value +"% off");

            }
            else
            {
                holder.product_price_.setText("Rs. "+productSub.product_price);
                holder.discount_pan.setVisibility(View.GONE);
                mCheckedStateA[position] = false;
            }


            if (productSub.measure_category.equalsIgnoreCase("Kilogram"))
            {
                holder.product_weight.setVisibility(View.VISIBLE);
                holder.product_weight.setText("/Kg");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Piece"))
            {
                holder.product_weight.setVisibility(View.VISIBLE);
                holder.product_weight.setText("/Pc");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Set"))
            {
                holder.product_weight.setVisibility(View.VISIBLE);
                holder.product_weight.setText("/Set");
            }
            else if (productSub.measure_category.equalsIgnoreCase("Carton"))
            {
                holder.product_weight.setVisibility(View.VISIBLE);
                holder.product_weight.setText("/Crt");
            }
            else
            {
                holder.product_weight.setVisibility(View.VISIBLE);
                holder.product_weight.setText("/Dzn");
            }





            if (!mCheckedStateA[position]) {

                holder.out_of_stock.setVisibility(View.GONE);
            } else {
              //  holder.out_of_stock.setVisibility(View.VISIBLE);
            }
            if(productSub.product_quan.equalsIgnoreCase("")||productSub==null)
            {
                quan=0;
            }
            else
            {
                quan=Integer.parseInt(productSub.product_quan);
            }


            mquan=Integer.parseInt(productSub.product_moq);
            if (quan<mquan||quan<0)
            {
                //holder.out_of_stock.setVisibility(View.VISIBLE);
                holder.discount_pan.setVisibility(View.GONE);


                mCheckedStateA[position] = true;

            }
            else
            {

                mCheckedStateA[position] = true;
            }

            holder.product_box_flashsale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {

                    Intent intent=new Intent(context, ProductProfileActivity.class);
                    intent.putExtra("flag","flag");
                    intent.putExtra("name",productSub.product_name);
                    intent.putExtra("path",productSub.product_image_path);
                    if (productSub.pstatus.equalsIgnoreCase("yes"))
                    {
                        intent.putExtra("price",productSub.product_promotionPrice);
                    }
                    else
                    {
                        intent.putExtra("price",productSub.product_price);
                    }

                    intent.putExtra("description",productSub.product_description);
                    intent.putExtra("product_id",productSub.product_id);
                    intent.putExtra("moq",productSub.product_moq);
                    intent.putExtra("quan",productSub.product_quan);
                    intent.putExtra("pro",productSub.product_provider);
                    intent.putExtra("measure_category",productSub.measure_category);
                    intent.putExtra("product_measure_in",productSub.product_measure_in);
                    context.startActivity(intent);
                }
            });


            // Glide.with(context).load(productSub.product_image_path).into(holder.ivFish);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return data.length();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout discount_pan,out_of_stock;
        CardView product_box_flashsale;
        ImageView ivFish,product_img;
        TextView discountoff,product_name_,product_price_,product_weight;
        MyHolder(View view)
        {
            super(view);
            product_weight=view.findViewById(R.id.product_weight);
            discount_pan=view.findViewById(R.id.discount_pan);
            out_of_stock=view.findViewById(R.id.out_of_stock);
            // ivFish=view.findViewById(R.id.ivFish_cons);
            product_price_=view.findViewById(R.id.product_price_);
            product_img=view.findViewById(R.id.product_img);
            discountoff=view.findViewById(R.id.discount_dexs);
            product_name_=view.findViewById(R.id.product_name_);
            product_box_flashsale=view.findViewById(R.id.product_box_flashsale);
        }
    }






    }



