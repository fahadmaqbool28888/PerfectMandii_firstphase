package com.vendor.perfectmandii.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vendor.perfectmandii.Activity.User.customerprofile.StoreProfile.AddStoreActivity;
import com.vendor.perfectmandii.DashboardActivity;
import com.vendor.perfectmandii.Model.Bank.BankModel;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class BankDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Context context;
    private LayoutInflater inflater;
    List<BankModel> data = Collections.emptyList();


    String category;


    // create constructor to innitilize context and data sent from MainActivity
    public BankDetail(Context context, List<BankModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.banktab, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        BankModel current = data.get(position);
        bydefault(myHolder);
        myHolder.radioButton.setText(current.name);
        myHolder.bankdescription.setText(current.ba_dec);





        //session=current.session;


    }

    void bydefault(MyHolder myHolder)
    {
        myHolder.bankdescription.setVisibility(View.GONE);
        myHolder.editText.setVisibility(View.GONE);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView bankdescription;
        RadioButton radioButton;
        EditText editText;



        public MyHolder(View itemView) {
            super(itemView);
            editText=itemView.findViewById(R.id.accountinput);
            radioButton=itemView.findViewById(R.id.banktitle);
            bankdescription=itemView.findViewById(R.id.bankinformation);


        }

    }
}