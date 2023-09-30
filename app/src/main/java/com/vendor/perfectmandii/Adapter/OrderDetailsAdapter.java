package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vendor.perfectmandii.DetailOrderModel;

import com.vendor.perfectmandii.R;

import java.util.ArrayList;

public class OrderDetailsAdapter extends ArrayAdapter<DetailOrderModel>
{
    Context context;
    ArrayList<DetailOrderModel> users;
  public  OrderDetailsAdapter(Context context, ArrayList<DetailOrderModel> users)
    {
     super(context,0,users);
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        DetailOrderModel user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.detail_product_order, parent, false);
       convertView= LayoutInflater.from(getContext()).inflate(R.layout.detail_product_order,parent,false);
        }
        TextView proeductName=convertView.findViewById(R.id.detailproduct_Name);
        TextView proeductQuantity=convertView.findViewById(R.id.detailproduct_Quantity);
        proeductName.setText(user.Product_Name);
        proeductQuantity.setText("QTY: "+user.order_quantity);
        return convertView;
    }
}