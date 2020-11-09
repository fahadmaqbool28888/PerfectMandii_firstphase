package com.consumer.perfectmandii.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.consumer.perfectmandii.Model.OrderCartModel;
import com.consumer.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class Customadaptor extends BaseAdapter {
    CheckBox checkBox;
    TextView priceitem, nameofitem, quanvalue, quantityofitem, productdes_1;

    ImageView ivFish;


    ImageView editfun, delefun;
    TextView productQuantity, sellingprice, productname, ordertotalprice;
    int deletepostion;
    boolean editflag=false;
    private final Context context;
    private final LayoutInflater inflater;
    List<OrderCartModel> data= Collections.emptyList();
    int count=0;
    String imagepath;
    String ocid;
    String editquantity,flag;
    int rposition;

    String current_price;

    public Customadaptor(Context context, List<OrderCartModel> data, String flag)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        this.flag=flag;
        this.rposition=rposition;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i)
    {

        return i;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        itemView = inflater.inflate(R.layout.list_rowv1, null);
        editfun=itemView.findViewById(R.id.ele);
        delefun=itemView.findViewById(R.id.dele);
        //
        checkBox=itemView.findViewById(R.id.idcheck_1);
        ivFish=itemView.findViewById(R.id.ivFish_1);
        sellingprice=itemView.findViewById(R.id.priceofitem);
        quantityofitem=itemView.findViewById(R.id.quantityofitem);
        productname=itemView.findViewById(R.id.nameofitem);
        ordertotalprice=itemView.findViewById(R.id.ordertotalprice);



        checkBox.setChecked(OrderCartModel.isIschecked());
        // Tag is important to get position clicked checkbox
        checkBox.setTag(i);
/*        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = (int) v.getTag();
                boolean isChecked = false;
                if (data.get(currentPos).isIschecked()==false){
                    isChecked=true;
                }
                Log.d("response ",currentPos+ " "+isChecked);
                data.get(currentPos).setIschecked(isChecked);
                notifyDataSetChanged();
            }
        });*/
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setChecked(OrderCartModel.isIschecked());
                // Tag is important to get position clicked checkbox
                checkBox.setTag(i);
                int currentPos = (int) buttonView.getTag();

                if (data.get(currentPos).isIschecked()==false){
                    isChecked=true;
                }
                Log.d("response ",currentPos+ " "+isChecked);
                data.get(currentPos).setIschecked(isChecked);
                notifyDataSetChanged();

            }
        });
        return itemView;
    }
}