package com.customer.perfectcustomer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
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
import com.customer.perfectcustomer.Model.ContentModel;
import com.customer.perfectcustomer.Model.SubCategories.ProductSub;
import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.ProductProfileActivity;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataContentAdapterMore extends RecyclerView.Adapter<DataContentAdapterMore.MyHolder>
        {     Double fr;
            int proqa;
        Context context;
        List<ContentModel> data;
        LayoutInflater layoutInflater;
            JSONArray data_array;

            List<ProductSub> productSubs;
            List<ProductSub> product;


            int quan,gp;
            int mquan;
            private final boolean[] mCheckedStateA;

            SparseBooleanArray[] checkstatearray;
public DataContentAdapterMore(Context context, JSONArray data_array)
        {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data_array=data_array;
            mCheckedStateA = new boolean[data_array.length()];
            checkstatearray=new SparseBooleanArray[data_array.length()];

        }


@Override
public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
{
        View view=layoutInflater.inflate(R.layout.container_flashsalemore,parent,false);
        MyHolder viewholder=new MyHolder(view);
        return viewholder;
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
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }

            @Override
public void onBindViewHolder(MyHolder holder, int position)
        {

            holder.setIsRecyclable(false);

            try {
                product=fetechjsonArray(data_array);
                ProductSub productSub=product.get(position);
                holder.product_name_.setText(productSub.product_name);
                Glide.with(context).load(productSub.product_image_path).into(holder.product_img);

                System.out.println(productSub.product_name);













                if (!mCheckedStateA[position])
                {


                }
                else {



                }
                if (quan<mquan)
                {
                    holder.out_of_stock.setVisibility(View.VISIBLE);
                    mCheckedStateA[position]=true;

                    holder.discount_pan.setVisibility(View.GONE);
                }
                else
                {
                    mCheckedStateA[position]=false;
                    if (productSub.pstatus.equalsIgnoreCase("yes"))
                    {
                        if (productSub.measure_category.equalsIgnoreCase("Kilogram"))
                        {
                          //  holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_price_.setText("Rs. "+productSub.product_promotionPrice);
                            Double price=getDiscountedp(productSub.product_promotionPrice,productSub.product_price);
                            int value= (int) (100-price);
                            holder.discountoff.setText(value +"% off");
                        }
                        else
                        {
                            holder.product_price_.setText("Rs. "+productSub.product_promotionPrice);
                            Double price=getDiscountedp(productSub.product_promotionPrice,productSub.product_price);
                            int value= (int) (100-price);
                            holder.discountoff.setText(value +"% off");
                        }


                    }
                 /*   else
                    {
                        if (productSub.measure_category!="Kilogram")
                        {
                           //holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                            holder.discount_pan.setVisibility(View.GONE);
                        }
                        else
                        {
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                            holder.discount_pan.setVisibility(View.GONE);

                        }

                    }*/

                    else if (productSub.pstatus.equalsIgnoreCase("no")||productSub.pstatus.equalsIgnoreCase(" "))
                    {
                       /* if (productSub.measure_category.equalsIgnoreCase("Kilogram"))
                        {
                            holder.product_weight.setVisibility(View.VISIBLE);


                        }
                        else
                        {
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                            holder.discount_pan.setVisibility(View.GONE);
                        }*/


                        if (productSub.measure_category.equalsIgnoreCase("Kilogram"))
                        {
                            holder.discount_pan.setVisibility(View.GONE);
                            holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_weight.setText("/Kg");
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                        }
                        else if (productSub.measure_category.equalsIgnoreCase("Piece"))
                        {
                            holder.discount_pan.setVisibility(View.GONE);
                            holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_weight.setText("/Pc");
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                        }
                        else if (productSub.measure_category.equalsIgnoreCase("Set"))
                        {
                            holder.discount_pan.setVisibility(View.GONE);
                            holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_weight.setText("/Set");
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                        }
                        else if (productSub.measure_category.equalsIgnoreCase("Carton"))
                        {
                            holder.discount_pan.setVisibility(View.GONE);
                            holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_weight.setText("/Crt");
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                        }
                        else
                        {
                            holder.discount_pan.setVisibility(View.GONE);
                            holder.product_weight.setVisibility(View.VISIBLE);
                            holder.product_weight.setText("/Dzn");
                            holder.product_price_.setText("Rs. "+productSub.product_price);
                        }

                    }





                    holder.product_box_flashsale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view)
                        {


                            mquan=Integer.parseInt(productSub.product_moq);
                            quan=Integer.parseInt(productSub.product_quan);
                            if (quan<mquan)
                            {
                               Toast.makeText(context,"Out of Stock",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                if (productSub.pstatus.equalsIgnoreCase("yes"))
                                {
                                    gp=Integer.parseInt(productSub.product_promotionPrice);
                                }
                                else
                                {
                                    if (productSub.product_price!=null)
                                    {
                                        gp=Integer.parseInt(productSub.product_price);
                                    }
                                    else
                                    {
                                        gp=0;
                                    }

                                }


                                if (gp>0)
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
                                    intent.putExtra("Prodcut_Sub_Category",productSub.Prodcut_Sub_Category);
                                    intent.putExtra("product_measure_in",productSub.product_measure_in);
                                    intent.putExtra("measure_category",productSub.measure_category);
                                    context.startActivity(intent);


                                }
                                else
                                {
                                    Toast.makeText(context,"This Product has lisiting issues",Toast.LENGTH_LONG).show();
                                }





                            }

                        }
                    });




                }






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
    ConstraintLayout discount_pan,out_of_stock;
    CardView product_box_flashsale;
    ImageView ivFish,product_img;
    TextView discountoff,product_name_,product_price_,product_weight;
    MyHolder(View view)
    {
        super(view);
        discount_pan=view.findViewById(R.id.discount_pan);
        out_of_stock=view.findViewById(R.id.out_of_stock);
        // ivFish=view.findViewById(R.id.ivFish_cons);
        product_price_=view.findViewById(R.id.product_price_);
        product_img=view.findViewById(R.id.product_img);
        discountoff=view.findViewById(R.id.discount_dexs);
        product_name_=view.findViewById(R.id.product_name_);
        product_box_flashsale=view.findViewById(R.id.product_box_flashsale);
        product_weight=view.findViewById(R.id.product_weight_1);
    }
}

            List<ProductSub> fetechjsonArray(JSONArray jsonArray) throws JSONException {


                productSubs=new ArrayList<>();
                for(int k=0;k<jsonArray.length();k++)
                {

                    JSONObject pr_data = jsonArray.getJSONObject(k);
                    ProductSub productSub=new ProductSub();
                    productSub.product_name=pr_data.getString("Product_Name");
                    productSub.product_id=pr_data.getString("id");
                    productSub.product_moq=pr_data.getString("moq");
                    productSub.product_description=pr_data.getString("Product_Description");
                    productSub.product_image_path=pr_data.getString("image_path");
                    productSub.product_price=pr_data.getString("Product_Unit_Price");
                    productSub.product_quan=pr_data.getString("Product_Unit_Quan");

                    productSub.product_provider=pr_data.getString("Product_Provider");
                    productSub.pstatus=pr_data.getString("promotion_status");
                    productSub.product_promotionPrice=pr_data.getString("promotion_price");
                    productSub.Prodcut_Sub_Category=pr_data.getString("Prodcut_Sub_Category");
                    productSub.Principle_Category=pr_data.getString("Principle_Category");
                    productSub.measure_category=pr_data.getString("measure_category");
                    productSub.product_measure_in=pr_data.getString("product_measure_in");


                    productSubs.add(productSub);
                }
                return productSubs;

            }
}
