package com.vendor.perfectmandii.Adapter.Payment.Bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.Bank.BankModel;
import com.vendor.perfectmandii.Model.OrderModel.ordermodel;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class bankPayment extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    List<BankModel> data= Collections.emptyList();
    ordermodel current;
    int currentPos=0;
    String category;
    String scategory;

    String[] av;
    String setca,pur;
    int id;
    String session;  // create constructor to innitilize context and data sent from MainActivity
    public bankPayment(Context context, List<BankModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_bankblock, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        BankModel current = data.get(position);

        Picasso.get().load(current.banklogo).into(myHolder.ivFish);
        myHolder.bankname.setText(current.name);
        myHolder.acctitle.setText("Account title:"+" "+current.branchAccTitle);
        myHolder.branchcode.setText("Branch Code:"+" "+current.branhcode);
        myHolder.accnumber.setText("Bank Account:"+" "+current.branchAccNum);
        //03377239311

    }




    // return total item from List
    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {
        CircleImageView ivFish;

        CardView mainvps;
        TextView bankname,acctitle,branchcode,accnumber;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);


            mainvps=(CardView)itemView.findViewById(R.id.mainsae);
            ivFish=  (CircleImageView) itemView.findViewById(R.id.ivFish);
            bankname=itemView.findViewById(R.id.branchname);
            acctitle=itemView.findViewById(R.id.acctitle);
            branchcode=itemView.findViewById(R.id.branchcode);
            accnumber=itemView.findViewById(R.id.ppdesc);


        }

    }





}
