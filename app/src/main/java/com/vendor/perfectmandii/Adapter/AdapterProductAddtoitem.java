package com.vendor.perfectmandii.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Model.DataFish;
import com.vendor.perfectmandii.Model.OrderCartModel;
import com.vendor.perfectmandii.Model.StockProductModel;
import com.vendor.perfectmandii.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AdapterProductAddtoitem extends RecyclerView.Adapter<AdapterProductAddtoitem.MyHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<StockProductModel> data= Collections.emptyList();
    DataFish current;
    int currentPos=0;
    String category;
    int count=0;
    private final boolean[] mCheckedStateA;

    SparseBooleanArray[] checkstatearray;

    int total,total1,value1;
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterProductAddtoitem(Context context, List<StockProductModel> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        mCheckedStateA = new boolean[data.size()];
        checkstatearray=new SparseBooleanArray[data.size()];
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=inflater.inflate(R.layout.list_rowv1, parent,false);
        MyHolder holder=new MyHolder(view);



        return holder;
    }


    public void removeItem(int pos)
    {
        data.remove(pos);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        StockProductModel current=data.get(position);
        Picasso.get().load(current.path).into(myHolder.produ_image);

        myHolder.available_stock.setText(current.stock);
        myHolder.product_moq.setText(current.MOQ);

        myHolder.stock_Value.setText("");

        myHolder.nameofitem.setText(current.name);




        myHolder.btn_price_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getvalue=myHolder.stock_Value.getText().toString();
                Intent intent = new Intent("custom-message");
                intent.putExtra("pos",current.name);
                intent.putExtra("id",current.id);
                intent.putExtra("astock",current.stock);
                intent.putExtra("mstock",getvalue);
                intent.putExtra("pos",position);
                intent.putExtra("flag","include");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });



        myHolder.ckick_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String getvalue=myHolder.stock_Value.getText().toString();
                Intent intent = new Intent("custom-message");
                intent.putExtra("pos",current.name);
                intent.putExtra("id",current.id);
                intent.putExtra("astock",current.stock);
                intent.putExtra("mstock",getvalue);
                intent.putExtra("pos",position);
                intent.putExtra("flag","include");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });


        myHolder.ckick_button_u.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String getvalue=myHolder.stock_Value.getText().toString();
                Intent intent = new Intent("custom-message");
                intent.putExtra("pos",current.name);
                intent.putExtra("id",current.id);
                intent.putExtra("astock",current.stock);
                intent.putExtra("mstock",getvalue);
                intent.putExtra("pos",position);
                intent.putExtra("flag","include");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });


/*        myHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getvalue=myHolder.stock_Value.getText().toString();
                Intent intent = new Intent("custom-message");
                if (!(mCheckedStateA[position])) {

                    myHolder.checkBox.setChecked(true);
                    mCheckedStateA[position]=true;

                    intent.putExtra("pos",current.name);
                    intent.putExtra("id",current.id);
                    intent.putExtra("astock",current.stock);
                    intent.putExtra("mstock",getvalue);
                    intent.putExtra("pos",position);
                    intent.putExtra("flag","include");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                } else {

                    myHolder.checkBox.setChecked(false);
                    mCheckedStateA[position]=false;
                    intent.putExtra("pos",current.name);
                    intent.putExtra("id",current.id);
                    intent.putExtra("astock",current.stock);
                    intent.putExtra("mstock",getvalue);
                    intent.putExtra("pos",position);

                    intent.putExtra("flag","exclude");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });*/


    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder
    {
        EditText stock_Value, available_stock ,product_moq;
        TextView stock_a,stock_e,miq_moq,nameofitem;
        ImageView produ_image;
        CheckBox checkBox;


        TextView btn_price_;
        CardView ckick_button;
        ConstraintLayout ckick_button_u;
        public MyHolder(View itemView) {
            super(itemView);
            stock_a=itemView.findViewById(R.id.stock_a);


            produ_image=itemView.findViewById(R.id.ivFish_1);
            stock_Value=itemView.findViewById(R.id.stock_value);

            ckick_button=itemView.findViewById(R.id.ckick_button);
            ckick_button_u=itemView.findViewById(R.id.inside_v);
            btn_price_=itemView.findViewById(R.id.btn_price_);


            available_stock=itemView.findViewById(R.id.available_stock);
            product_moq=itemView.findViewById(R.id.product_moq);
            nameofitem=itemView.findViewById(R.id.nameofitem);

        }

    }

}