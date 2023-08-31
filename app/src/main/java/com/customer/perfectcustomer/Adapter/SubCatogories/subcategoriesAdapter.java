package com.customer.perfectcustomer.Adapter.SubCatogories;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.customer.perfectcustomer.Activity.Home.ProductScreen.DisplayProduct.Home.Fragment.ProductBy.Category.loadmoreActivity;
import com.customer.perfectcustomer.Model.SubCategories.SubCategories;
import com.customer.perfectcustomer.R;

import java.util.List;

public class subcategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    LayoutInflater layoutInflater;
    List<SubCategories> data;
    Context context;
    subItemAdapter subitem;


    public subcategoriesAdapter(Context context, List<SubCategories> data)
    {
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.data=data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.item_undercategory,parent,false);
        MyHolder myHolder= new MyHolder(view);

        return myHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {





        MyHolder myHolder= (MyHolder) holder;
        SubCategories current=data.get(position);
                //CategoyByProductModel current=data.get(position);
        String str=current.name.replaceAll("[^a-zA-Z]"," ");
        myHolder.categoryName_panel.setText(str);

        myHolder.Loadmore_panel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent=new Intent(context, loadmoreActivity.class);
                intent.putExtra("jsonArray", current.jsonArray.toString());
                intent.putExtra("li",current.parent_Category);
                intent.putExtra("userid",current.userid);
                intent.putExtra("session",current.session);
                intent.putExtra("categoryname",current.name);
                intent.putExtra("vi",current.isa);
                context.startActivity(intent);
            }
        });




        subitem=new subItemAdapter(context,current.jsonArray,current.userid,current.session,current.name);

        myHolder.recyclerView.setAdapter(subitem);
        myHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        TextView categoryName_panel,Loadmore_panel;
        RecyclerView recyclerView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            categoryName_panel=itemView.findViewById(R.id.categoryName_panel);
            recyclerView=itemView.findViewById(R.id.subitem);
            Loadmore_panel=itemView.findViewById(R.id.Loadmore_panel);
        }
    }



}
