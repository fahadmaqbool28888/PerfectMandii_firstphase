package com.vendor.perfectmandii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ConfirmOrderitem extends AppCompatActivity
{

    String session;
    String userid;
    CardView cardView;
    TextView textView;
    //userid
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent intent=getIntent();
        userid=intent.getStringExtra("userid");
        session=intent.getStringExtra("session");

        Toast.makeText(ConfirmOrderitem.this,"This is"+session,Toast.LENGTH_LONG).show();



        cardView=findViewById(R.id.cts);
        textView=findViewById(R.id.ts);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1=new Intent(ConfirmOrderitem.this,MainActivity.class);
                intent1.putExtra("session",session);
                intent1.putExtra("userid",userid);
                startActivity(intent1);

            }
        });

    }

    String  createsession()
    {
        Calendar cal = Calendar.getInstance();
        String cs=cal.getTime().toString();
        String  input = cs;
        input = input.replace(" ", "");
        String input1 = input.replace("+", "");
        String input2 = input1.replace(":", "");
        return input2;
    }
}