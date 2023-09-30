package com.vendor.perfectmandii.ProfileFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vendor.perfectmandii.CustomViewPager;
import com.vendor.perfectmandii.FragmentChangeListener;
import com.vendor.perfectmandii.Model.StoreData.StoreModel;
import com.vendor.perfectmandii.R;
import com.vendor.perfectmandii.profile_Updates.ProfileInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment implements FragmentChangeListener {

    private static final int MODE_PRIVATE = 196;
    List<StoreModel> store_list;
    TextView next_button;
    EditText store_name, store_abbr, store_desc, warehouse_address, shipping_address;
    CheckBox same_Asabove;
    public CustomViewPager mViewPager;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("myKey", "myValue"); // trivial, but for illustration purposes.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_store, container, false);
        // Inflate the layout for this fragment

        intializeWidget(view);
        store_abbr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                identifyString(store_name.getText().toString());

            }
        });
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkempty();


            }
        });

        same_Asabove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (same_Asabove.isChecked()) {
                    shipping_address.setText(warehouse_address.getText().toString());
                } else {
                    shipping_address.setText("");
                }
            }
        });


        return view;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(new StoreFragment());
        fragmentTransaction.replace(R.id.fragment_store, fragment, fragment.toString());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    void intializeWidget(View view) {
        next_button = view.findViewById(R.id.next_button);
        store_name = view.findViewById(R.id.store_name);
        store_abbr = view.findViewById(R.id.store_abbr);
        store_desc = view.findViewById(R.id.store_desc);
        warehouse_address = view.findViewById(R.id.warehouse_address);
        shipping_address = view.findViewById(R.id.shipping_address);
        same_Asabove = view.findViewById(R.id.same_Asabove);

    }

    void getData() {
        store_list = new ArrayList<>();
        StoreModel storeModel = new StoreModel();
        storeModel.store_name = store_name.getText().toString();
        storeModel.store_abbreviation = store_abbr.getText().toString();
        storeModel.store_description = store_desc.getText().toString();
        storeModel.warehouse_address = warehouse_address.getText().toString();
        storeModel.shipping_address = shipping_address.getText().toString();
        store_list.add(storeModel);
    }

    void checkempty() {
        if (store_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Enter Store Name", Toast.LENGTH_LONG).show();
        } else if (store_abbr.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Enter Store Abbreviation", Toast.LENGTH_LONG).show();
        } else if (store_desc.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Enter Store Description", Toast.LENGTH_LONG).show();
        } else if (warehouse_address.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Enter Warehouse Address", Toast.LENGTH_LONG).show();
        } else if (shipping_address.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Enter Shipping Address", Toast.LENGTH_LONG).show();
        } else {
            getData();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            ContactFragment hallsForState = new ContactFragment();
            Bundle args = new Bundle();
            args.putString("storename", store_name.getText().toString());
            args.putString("sa", store_abbr.getText().toString());
            args.putString("storedesc", store_desc.getText().toString());
            args.putString("ShippingAddress", shipping_address.getText().toString());
            args.putString("WarehouseAddress", warehouse_address.getText().toString());
            hallsForState.setArguments(args);

// Storing data into SharedPreferences
            @SuppressLint("WrongConstant") SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
            myEdit.putString("storename", store_name.getText().toString());
            myEdit.putString("sa", store_abbr.getText().toString());
            myEdit.putString("storedesc", store_desc.getText().toString());
            myEdit.putString("ShippingAddress", shipping_address.getText().toString());
            myEdit.putString("WarehouseAddress", warehouse_address.getText().toString());

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
            myEdit.commit();
            transaction.replace(((ViewGroup) getView().getParent()).getId(), hallsForState);
            transaction.addToBackStack(null);
            transaction.commit();

            ProfileInformation profileInformation = (ProfileInformation) getActivity();
            profileInformation.Recieve_Data("Done_1", store_list);
        }
    }


    void identifyString(String str) {
        String[] phones = str.split("\\s+");

        if (phones.length > 1) {

            String str1 = phones[0].substring(0, 1);
            String str2 = phones[1].substring(0, 1);
            store_abbr.setText(str1.toUpperCase() + str2.toUpperCase());
        } else {
            String str1 = phones[0].substring(0, 2);
            //   String str2=phones[1].substring(0,1);
            store_abbr.setText(str1.toUpperCase());
        }

    }




}