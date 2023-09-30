package com.vendor.perfectmandii.CategoryDetail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.CategoryDetail.Model.ProductModel;
import com.vendor.perfectmandii.R;

import java.util.ArrayList;

public abstract class binstate extends RecyclerView.Adapter<binstate.Myholder> {


    private final boolean[] mCheckedStateA;
    Context context;
    ArrayList<ProductModel> productModelArrayList;
    LayoutInflater layoutInflater;
    boolean flag;
    boolean isallselected=false;



    public binstate(Context context, ArrayList<ProductModel> productModelArrayList, boolean flag) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.productModelArrayList = productModelArrayList;
        this.flag=flag;
        mCheckedStateA = new boolean[productModelArrayList.size()];

    }


    public void selectAllItems() {
        for (ProductModel item : productModelArrayList) {
            item.setSelected(true);
        }
        isallselected=true;
        notifyDataSetChanged();
    }

    public void unselectAllItems() {
        for (ProductModel item : productModelArrayList) {
            item.setSelected(false);
        }
        isallselected=false;
        notifyDataSetChanged();
    }
    public abstract void add(String action,ProductModel productModel);
    public abstract void minus(ProductModel productModel);
    public abstract void unselectAll(String action,ProductModel productModel);

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.price_row, parent, false);
        Myholder myholder = new Myholder(view);
        return myholder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {

        ProductModel current = productModelArrayList.get(position);

        holder.nameofiteml.setText(current.name);
        holder.price.setText(current.price);

        if (current.path.contains("https://sellerportal.perfectmandi.com/"))
        {
            Picasso.get().load(current.path).into(holder.imageView);

        }
        else {
            String url="https://sellerportal.perfectmandi.com/"+current.path;
            Picasso.get().load(url).into(holder.imageView);

        }
        holder.add_them.setOnCheckedChangeListener(null);





        holder.add_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.add_them.isChecked())
                {
                    if (isallselected)
                    {


                    }
                    else {
                        add("add",current);
                        current.setSelected(true);
                    }
                }
                else {
                    if (isallselected)
                    {
                        isallselected=false;
                        // add("remove",current);
                        unselectAll("r",current);
                        current.setSelected(false);

                    }
                    else {
                        minus(current);
                        current.setSelected(false);
                    }

                }
            }
        });
        if (current.isSelected())
        {
            holder.add_them.setChecked(true);
        }
     else {
            holder.add_them.setChecked(false);
        }




        if (current.measure_category.equalsIgnoreCase("Kilogram"))
        {
            holder.price.setText("PKR. "+current.price+" /Kg");

        }
        else if (current.measure_category.equalsIgnoreCase("Piece"))
        {
            holder.price.setText("PKR. "+current.price+" /Pc");

        }
        else if (current.measure_category.equalsIgnoreCase("Set"))
        {
            holder.price.setText("PKR. "+current.price+" /Set");

        }
        else if (current.measure_category.equalsIgnoreCase("Carton"))
        {
            holder.price.setText("PKR. "+current.price+" /Crt");

        }
        else
        {
            holder.price.setText("PKR. "+current.price+" /Dzn");

        }


    }


    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

        CheckBox add_them;
        ImageView imageView;
        TextView nameofiteml, price;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagePath_pro);
            nameofiteml = itemView.findViewById(R.id.nameofitem_);
            price = itemView.findViewById(R.id.priceofitem_);
            add_them = itemView.findViewById(R.id.add_them);
        }
    }
}


