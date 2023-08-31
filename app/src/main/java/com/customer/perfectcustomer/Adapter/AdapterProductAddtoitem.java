package com.customer.perfectcustomer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.customer.perfectcustomer.Model.OrderCartModel;
import com.customer.perfectcustomer.R;


import java.util.Collections;
import java.util.List;



public abstract class AdapterProductAddtoitem extends RecyclerView.Adapter<AdapterProductAddtoitem.MyHolder> {
    boolean isallselected=false;

    String current_price, selling_price, quantityofitem;
    String link;
    public boolean isAllChecked = false;
    int deletepostion;
    boolean editflag=false;
    private final Context context;
    private final LayoutInflater inflater;
    List<OrderCartModel> data= Collections.emptyList();
    int count=0;
    String imagepath;
    String ocid;
    String editquantity;
    boolean flag;
    int rposition;






    public class MyHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView priceitem,nameofitem,quanvalue,quantityofitem,productdes_1;

        ImageView ivFish;


        ImageView editfun,delefun;
        TextView productQuantity,sellingprice,productname,ordertotalprice;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            //
            editfun=itemView.findViewById(R.id.ele);
            delefun=itemView.findViewById(R.id.dele);
            //
            checkBox=itemView.findViewById(R.id.idcheck_1);
            ivFish=itemView.findViewById(R.id.ivFish_1);
            sellingprice=itemView.findViewById(R.id.priceofitem);
            quantityofitem=itemView.findViewById(R.id.quantityofitem);
            productname=itemView.findViewById(R.id.nameofitem);
            ordertotalprice=itemView.findViewById(R.id.ordertotalprice);

        }


    }

    public abstract  void update_val(String flag,OrderCartModel orderCartModel);

    public AdapterProductAddtoitem(Context context, List<OrderCartModel> data,boolean flag)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.flag=flag;
        this.rposition=rposition;
       // mCheckedStateA = new boolean[data.size()];




    }

    public void selectAllItems() {
        for (OrderCartModel item : data) {
            item.setSelected(true);
        }
        isallselected=true;
        notifyDataSetChanged();
    }

    public void unselectAllItems() {
        for (OrderCartModel item : data) {
            item.setSelected(false);
        }
        isallselected=false;
        notifyDataSetChanged();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.list_rowv1, parent,false);
        MyHolder holder=new MyHolder(view);



        return holder;
    }





    boolean isEditflag=false;
    // Bind data
    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") int position)
    {

        OrderCartModel current = data.get(position);
        holder.productname.setText(current.name);
        holder.checkBox.setChecked(current.isSelected());


        if (current.image_urlname.contains("https://sellerportal.perfectmandi.com/"))
        {
            Glide
                    .with(context)
                    .load(current.image_urlname)
                    .centerCrop()
                    .into(holder.ivFish);
        }
        else
        {
            String url="https://sellerportal.perfectmandi.com/"+current.image_urlname;
            Glide
                    .with(context)
                    .load(url)
                    .centerCrop()
                    .into(holder.ivFish);
        }




        imagepath = current.image_urlname;

        selling_price = current.selling_price;
        quantityofitem = current.order_quantity;
        holder.sellingprice.setText("Price: "+selling_price);
        holder.quantityofitem.setText("Quantity: "+quantityofitem);
        if (current.measure_category.equalsIgnoreCase("Kilogram")) {
            Double total = Double.parseDouble(selling_price) * Double.parseDouble(quantityofitem) * Double.parseDouble(current.product_measure_in);

            holder.ordertotalprice.setText("Total: " + total);
        } else {
            Double total = Double.parseDouble(selling_price) * Double.parseDouble(quantityofitem);
            holder.ordertotalprice.setText("Total: " + total);

        }

        holder.checkBox.setOnCheckedChangeListener(null); // Remove previous listener to prevent unwanted triggering


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked())
                {
                    current.setSelected(true);
                    passitem("include",current);

                }
                else
                {
                    if (isallselected)
                    {
                        removeItems(position,current);

                        current.setSelected(false);
                    }
                    else {
                        passitem("exclude",current);

                        current.setSelected(false);
                    }

                }
            }
        });


        if (current.isSelected())
        {
            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }

        holder.editfun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_val("edit",current);
            }
        });

        holder.delefun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_val("del",current);
            }
        });




    }


    public  abstract void removeItems(int pos,OrderCartModel orderCartModel);

    public abstract  void passitem(String flag, OrderCartModel orderCartModel);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getCount() {
        return data.size();
    }

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;


}

