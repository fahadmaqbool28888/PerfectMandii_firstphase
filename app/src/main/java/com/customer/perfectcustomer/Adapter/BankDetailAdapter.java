package com.customer.perfectcustomer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Model.Bank.BankModel;
import com.customer.perfectcustomer.R;

import java.util.List;

public class BankDetailAdapter extends RecyclerView.Adapter
{

    Context context;
    List<BankModel> bankModels;
    LayoutInflater layoutInflater;


    public BankDetailAdapter(Context context, List<BankModel> bankModels)
    {
        this.bankModels=bankModels;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=layoutInflater.inflate(R.layout.payment_option_container,parent,false);
        MyHolder myHolder= new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position)
    {
        BankModel bankModel=bankModels.get(position);
        MyHolder myHolder= (MyHolder) holder;
        myHolder.bankname.setText(bankModel.name);
        myHolder.banlcode.setText(bankModel.branchAccNum+ "\n"+ bankModel.branchAccTitle);

    }

    @Override
    public int getItemCount()
    {
        return bankModels.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder
    {

        TextView bankname,banlcode;
        public MyHolder(View itemView) {
            super(itemView);
            bankname=itemView.findViewById(R.id.bankname);
            banlcode=itemView.findViewById(R.id.banlcode);
        }
    }
}
