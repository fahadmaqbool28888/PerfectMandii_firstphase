package com.consumer.perfectmandii;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.consumer.perfectmandii.Adapter.DataContentAdapter;
import com.consumer.perfectmandii.Adapter.DataContentAdapter_FlashSale;
import com.consumer.perfectmandii.Customer.UserModel;
import com.consumer.perfectmandii.LocalDB.SQLiteHelper;
import com.consumer.perfectmandii.Model.ModelDas;
import com.consumer.perfectmandii.Model.SubCategories.ProductSub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderConsigneeAdapter extends RecyclerView.Adapter<OrderConsigneeAdapter.MyHolder>
{
    Context context;

    LayoutInflater layoutInflater;
    DataContentAdapter dataContentAdapter;
    DataContentAdapter_Cat dataContentAdapter_cat;
    DataContentAdapter_FlashSale dataContentAdapter_flashSale;

    ArrayList<DeliverDetail> productSubs;
    JSONArray jsonArray;

    ArrayList<DeliverDetail> fetechjsonArray(JSONArray jsonArray) throws JSONException
    {
        productSubs=new ArrayList<>();
        for(int k=0;k<jsonArray.length();k++)
        {
            JSONObject pr_data = jsonArray.getJSONObject(k);
            DeliverDetail productSub=new DeliverDetail();

            productSub.amount=pr_data.getString("amount");
            productSub.image_path=pr_data.getString("image_path");
            productSub.purchaseInvoice=pr_data.getString("purchaseInvoice");
            productSub.status=pr_data.getString("status");

            productSubs.add(productSub);
        }
        return productSubs;

    }
    public OrderConsigneeAdapter(Context context, JSONArray jsonArray)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

        this.jsonArray=jsonArray;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.order_detail_plan,parent,false);
        MyHolder viewholder=new MyHolder(view);

        //get_Data();
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        try
        {
            productSubs=fetechjsonArray(jsonArray);
            DeliverDetail modelTemp=productSubs.get(position);

            System.out.println(modelTemp.purchaseInvoice);
            System.out.println(modelTemp.amount);
            System.out.println(modelTemp.image_path);
            System.out.println(modelTemp.status);
            holder.invoice_num.setText(String.valueOf(position+1));
            holder.invoice_total.setText(modelTemp.amount);
            if (modelTemp.status.equalsIgnoreCase("delivered"))
            {
                holder.status_sub.setText("In Transit");
            }
            else
            {
                holder.status_sub.setText("Completed");
            }

            holder.status_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.status_sub.getText().equals("In Transit"))
                    {
                        Intent intent = new Intent("custom-message");


                         intent.putExtra("in",modelTemp.purchaseInvoice);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);



                    }
                    else
                    {
                        Toast.makeText(context, "4", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            holder.status_sub_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1=new Intent(context,ViewImageActivity.class);
                    String url="https://sellerportal.perfectmandi.com/";
                    intent1.putExtra("path",url+modelTemp.image_path);
                    context.startActivity(intent1);
                }
            });

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }










        //holder.heading_co.setText(contentModel.di_hname);
    }

    @Override
    public int getItemCount()
    {
        return jsonArray.length();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
    TextView invoice_num,invoice_total ,status_sub;
    ImageView status_sub_view;
        MyHolder(View view)
        {
            super(view);
            invoice_num=view.findViewById(R.id.invoice_num);
            invoice_total=view.findViewById(R.id.invoice_total);
            status_sub=view.findViewById(R.id.status_sub);
            status_sub_view=view.findViewById(R.id.status_sub_view);
        }
    }
}
