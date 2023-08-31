package com.customer.perfectcustomer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Activity.Home.VendorStore.VendorStoreActivity;
import com.customer.perfectcustomer.Model.Vendor_Model.Vendor_Prime;
import com.customer.perfectcustomer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataContentAdapter_Vendor extends RecyclerView.Adapter<DataContentAdapter_Vendor.MyHolder>
{

    Context context;
    LayoutInflater layoutInflater;
    JSONArray data_array;
    ArrayList<Vendor_Prime> vendor_primes;
    List<Vendor_Prime> vendorprimes;
    String url="https://sellerportal.perfectmandi.com/";
    String imagepath;
    DataContentAdapter_Vendor(Context context,JSONArray data_array)
    {
        this.context=context;
        layoutInflater= LayoutInflater.from(context);
        this.data_array=data_array;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view=layoutInflater.inflate(R.layout.container_vendor_ali,parent,false);
        MyHolder viewholder=new MyHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {


        try {

            vendorprimes = fetechjsonArray(data_array);


            Vendor_Prime vendor_prime = vendorprimes.get(position);
          //  System.out.println(vendor_prime.image_url);
          //  imagepath=url+vendor_prime.image_url;
            holder.storename_vendor.setText(vendor_prime.name);
            holder.storecity_vendor.setText(vendor_prime.city);

            if (vendor_prime.image_url.contains("https://sellerportal.perfectmandi.com/"))
            {
                Glide.with(context).load(vendor_prime.image_url).into(holder.product_img);
            }
            else
            {
                String urls="https://sellerportal.perfectmandi.com/"+vendor_prime.image_url;
                Glide.with(context).load(urls).into(holder.product_img);

            }
            //  Glide.with(context).load(imagepath).into(holder.product_img);
            //
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent1=new Intent(context, VendorStoreActivity.class);
                    intent1.putExtra("pro",vendor_prime.id);
                    intent1.putExtra("Sub_Category","");
                    context.startActivity(intent1);
                }
            });


        }
        catch (Exception exception)
        {

        }

    }

    @Override
    public int getItemCount() {
        if (data_array.length()<10)
        {
            return data_array.length();
        }
        else
        {
            return 10;
        }
    }

    public class  MyHolder extends RecyclerView.ViewHolder
    {
        CircleImageView product_img;
        TextView storename_vendor ,storecity_vendor;

        CardView cardView;
        public MyHolder(View itemView)
        {
            super(itemView);
            product_img=itemView.findViewById(R.id.store_logo_1);
            storename_vendor=itemView.findViewById(R.id.storename_vendor);
            storecity_vendor=itemView.findViewById(R.id.storecity_vendor);
            cardView=itemView.findViewById(R.id.vendor_wala_dabba);
        }
    }

    List<Vendor_Prime> fetechjsonArray(JSONArray jsonArray) throws JSONException {


        vendor_primes=new ArrayList<>();
        for(int k=0;k<jsonArray.length();k++)
        {
            JSONObject pr_data = jsonArray.getJSONObject(k);
            Vendor_Prime productSub=new Vendor_Prime();
            // productSub.product_id=pr_data.getString("id");
            // "id": "8", "name": "Capital Crockery Store", "image_path": "uploads\/vendor\/document\/03455232823\/Toys Icon.webp", "city": "Rawalpindi"
            productSub.id=pr_data.getString("id");

            productSub.name=pr_data.getString("name");
            productSub.city=pr_data.getString("city");

            productSub.image_url=pr_data.getString("image_path");


            vendor_primes.add(productSub);
        }
        return vendor_primes;

    }

}
