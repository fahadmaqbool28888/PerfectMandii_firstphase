package com.vendor.perfectmandii.Adapter.Instruction.PhotoUpload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendor.perfectmandii.Adapter.AdapterHome;
import com.vendor.perfectmandii.Model.Instruction.instructionmodel;
import com.vendor.perfectmandii.Model.vendor.vendorServiceModel;
import com.vendor.perfectmandii.R;

import java.util.Collections;
import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private LayoutInflater inflater;
    List<instructionmodel> data= Collections.emptyList();



   public InstructionsAdapter(Context context,List<instructionmodel> data)
    {
        this.data=data;
        this.context=context;
        this.inflater=LayoutInflater.from(context);



    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View view=inflater.inflate(R.layout.layout_instruction,parent,false);
       MyHolder myholder=new  MyHolder(view);
       return myholder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position)
    {
        MyHolder myHolder= (MyHolder) holder;
        instructionmodel model=data.get(position);
        myHolder.instuction_id.setText(model.id);
        myHolder.instuction_text.setText(model.text);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {

        TextView instuction_id,instuction_text;

        public MyHolder(View itemView)
        {
            super(itemView);
            instuction_id= itemView.findViewById(R.id.instuction_id);
            instuction_text=itemView.findViewById(R.id.instuction_text);

        }

    }
}
